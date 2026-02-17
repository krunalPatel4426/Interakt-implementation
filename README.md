# High-Performance Bulk Message Scheduler (Pure Spring Boot)

## Overview
This project implements a production-grade, high-performance scheduling system designed to send thousands of WhatsApp messages via the Interakt API. 

It uses a **"Pure Spring Boot"** architecture (no Kafka/RabbitMQ required) that leverages **Reactive Programming (WebFlux)** to handle high concurrency with minimal memory usage. It is designed to be "Crash Proof" and strictly prevents double-sending.

## Key Features
* **Zero External Dependencies:** Runs entirely on Spring Boot + MySQL.
* **Non-Blocking I/O:** Uses `WebClient` to send 1000s of messages using only 1 thread.
* **Concurrency Control:** Limits API calls to 50 concurrent requests to respect Interakt rate limits.
* **Atomic Locking:** Implements a "Fetch-Lock-Process" pattern to ensure no message is ever sent twice.
* **Batch Database Updates:** Updates message statuses in chunks (e.g., 50 at a time) to prevent database thrashing.
* **Jitter Tolerance:** Handles millisecond mismatches between the Scheduler and the DB clock.

---

## Architecture Flow

The system runs on a **Polling Cycle** (every 10 seconds) using the `Fetch-Lock-Process` pattern:

1.  **Wake Up:** The Scheduler triggers every 10 seconds (`@Scheduled(fixedDelay = 10000)`).
2.  **Fetch & Buffer:** It queries the DB for messages with status `PENDING` that are due `NOW` (or in the next 5 seconds).
3.  **Atomic Lock:** It **immediately** updates the status of these rows to `PROCESSING`. 
    * *Why?* This ensures that if the next scheduler runs while the current one is still sending, it won't pick up the same messages.
4.  **Reactive Stream:** The messages are processed in a Reactive Stream (`Flux`):
    * **Parallelism:** Up to 50 requests are sent to Interakt simultaneously.
    * **Non-Blocking:** The thread does not wait for the API response; it moves to the next task immediately.
5.  **Batch Commit:** As responses come back, they are buffered into groups of 50 and saved to the DB as `SENT` or `FAILED`.

---

## Configuration

### 1. Dependencies (`pom.xml`)
You must include Spring WebFlux for `WebClient`.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

```

### 2. Properties (`application.properties`)

Enable JDBC batching to ensure `saveAll()` is efficient.

```properties
# Efficient Batch Updates
spring.jpa.properties.hibernate.jdbc.batch_size=50
spring.jpa.properties.hibernate.order_updates=true
spring.jpa.properties.hibernate.batch_versioned_data=true

```

---

## Core Components & Logic

### 1. The Repository (`ScheduleMessageRepository`)

We use a custom query to fetch messages and a Modifying query to lock them efficiently.

* **Fetch:** `findReadyMessages` (Status = PENDING, Time <= Now + 5s)
* **Lock:** `updateStatusForIds` (Updates status to PROCESSING for a list of IDs)

### 2. The Service (`ReliableScheduleService`)

This is the core engine.

* **Annotation:** `@Scheduled(fixedDelay = 10000)`
* *Note:* We use `fixedDelay` instead of Cron to ensure the previous batch finishes before the next starts, preventing overlap.


* **Jitter Fix:** `LocalDateTime.now().plusSeconds(5)`
* *Reason:* If the scheduler runs at `10:00:00.000` but the message is `10:00:00.005`, a standard query misses it. Adding 5 seconds ensures we catch everything due "right now".


* **Concurrency:** `.flatMap(..., 50)`
* Limits active connections to Interakt to 50.


* **Thread Switching:** `.publishOn(Schedulers.boundedElastic())`
* Ensures that the blocking Database `saveAll()` call does not freeze the non-blocking HTTP thread.



### 3. The Client (`InteraktClient`)

* Uses `WebClient` instead of `RestTemplate`.
* Returns a `Mono<MessageDeliveryResult>`.
* Handles errors gracefully using `.onErrorResume` so one failed message does not crash the entire batch.

---

## Database Schema (`ScheduledMessageEntity`)

| Column Name | Type | Description |
| --- | --- | --- |
| `id` | BIGINT | Primary Key |
| `phone_number` | VARCHAR | Recipient Phone |
| `json_payload` | TEXT | The full JSON body to send to Interakt |
| `schedule_time` | DATETIME | When the message should be sent |
| `status` | VARCHAR | `PENDING`, `PROCESSING`, `SENT`, `FAILED` |
| `error_message` | TEXT | Stores the API error response if failed |

**Recommended Index:**
To keep polling fast as the table grows, execute this SQL:

```sql
CREATE INDEX idx_status_time ON schedule_message (status, schedule_time);

```

---

## How to Schedule a Message

Simply save the entity to the database. The Poller will pick it up automatically.

```java
public void scheduleMessage(InteraktTemplateRequestDto request) {
    ScheduledMessageEntity entity = new ScheduledMessageEntity();
    entity.setPhoneNumber(request.getPhoneNumber());
    entity.setJsonPayload(objectMapper.writeValueAsString(request));
    entity.setScheduleTime(request.getScheduledTime()); // e.g., 2026-02-20 10:00:00
    entity.setStatus("PENDING");
    
    repository.save(entity);
    // No extra code needed. The Scheduler will find it at 10:00:00.
}

```

## Troubleshooting

**Q: Why does it sometimes send 1-5 seconds early?**
A: We fetch messages due in the `now() + 5s` window to prevent missing messages due to millisecond mismatches. This is acceptable for marketing/notification messages.

**Q: What if the server crashes while status is PROCESSING?**
A: The messages will remain stuck in `PROCESSING`.

* *Fix:* You can run a manual DB script to reset stuck messages: `UPDATE schedule_message SET status='PENDING' WHERE status='PROCESSING' AND schedule_time < NOW() - INTERVAL 1 HOUR;`

**Q: Can I increase the speed?**
A: Yes. In the service, increase the concurrency limit in `.flatMap(..., 100)` and the batch size in `.bufferTimeout(100, ...)` if your Interakt plan allows it.

