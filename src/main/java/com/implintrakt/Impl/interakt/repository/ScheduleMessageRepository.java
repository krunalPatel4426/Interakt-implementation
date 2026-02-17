    package com.implintrakt.Impl.interakt.repository;

    import com.implintrakt.Impl.interakt.dto.projection.scheduleMessage.ScheduleMessageProjection;
    import com.implintrakt.Impl.interakt.model.ScheduledMessageEntity;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Modifying;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Repository;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.LocalDateTime;
    import java.util.List;

    @Repository
    public interface ScheduleMessageRepository extends JpaRepository<ScheduledMessageEntity, Long> {
//        @Query("SELECT m FROM ScheduledMessageEntity m WHERE m.status = :status AND m.scheduleTime <= :currentTime")
        @Query(nativeQuery = true, value = """
        SELECT sm.id as id,
               sm.json_payload as jsonPayload,
               sm.phone_number as PhoneNumber,
               sm.schedule_time as scheduleTime,
               sm.status as status,
               sm.error_message as errorMessage
        from schedule_message sm where sm.status = :status AND sm.schedule_time <= :currentTime;
""")
        List<ScheduleMessageProjection> findReadyMessages(
                @Param("status") String status,
                @Param("currentTime") LocalDateTime currentTime
        );

        @Modifying
        @Transactional
        @Query("UPDATE ScheduledMessageEntity m SET m.status = :newStatus WHERE m.id IN :ids")
        void updateStatusForIds(@Param("ids") List<Long> ids, @Param("newStatus") String newStatus);

        List<ScheduledMessageEntity> findByStatus(String interaktStatusPending);
    }
