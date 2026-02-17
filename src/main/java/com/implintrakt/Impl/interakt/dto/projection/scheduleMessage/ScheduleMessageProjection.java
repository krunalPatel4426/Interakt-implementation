package com.implintrakt.Impl.interakt.dto.projection.scheduleMessage;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public interface ScheduleMessageProjection {
    Long getId();
    String getJsonPayload();
    String getPhoneNumber();
    LocalDateTime getScheduleTime();
    String getStatus();
    String getErrorMessage();
}
