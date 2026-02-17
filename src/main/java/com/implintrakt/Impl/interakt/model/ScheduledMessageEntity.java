package com.implintrakt.Impl.interakt.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "schedule_message")
public class ScheduledMessageEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "json_payload", columnDefinition = "TEXT")
    private String jsonPayload;

    @Column(name = "schedule_time")
    private LocalDateTime scheduleTime;

    @Column(name = "status")
    private String status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
}
