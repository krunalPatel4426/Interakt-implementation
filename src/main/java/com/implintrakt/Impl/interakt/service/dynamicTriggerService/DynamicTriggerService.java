package com.implintrakt.Impl.interakt.service.dynamicTriggerService;

import java.time.LocalDateTime;

public interface DynamicTriggerService {
    void init();
    void scheduleWakeUpCall(LocalDateTime scheduleTime);
}
