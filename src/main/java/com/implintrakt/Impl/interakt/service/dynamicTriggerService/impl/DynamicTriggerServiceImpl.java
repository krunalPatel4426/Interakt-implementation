package com.implintrakt.Impl.interakt.service.dynamicTriggerService.impl;

import com.implintrakt.Impl.interakt.config.Constants.Constant;
import com.implintrakt.Impl.interakt.model.ScheduledMessageEntity;
import com.implintrakt.Impl.interakt.repository.ScheduleMessageRepository;
import com.implintrakt.Impl.interakt.service.dynamicTriggerService.DynamicTriggerService;
import com.implintrakt.Impl.interakt.service.scheduleService.ReactiveScheduleService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Scheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class DynamicTriggerServiceImpl implements DynamicTriggerService {
    private final Logger logger = LoggerFactory.getLogger(DynamicTriggerServiceImpl.class);

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private ScheduleMessageRepository  scheduleMessageRepository;

    @Autowired
    private ReactiveScheduleService  reactiveScheduleService;

    @PostConstruct
    public void init() {
        logger.info("Initializing DynamicTriggerServiceImpl");
        List<ScheduledMessageEntity> pending = scheduleMessageRepository.findByStatus(Constant.INTERAKT_STATUS_PENDING);

        for (ScheduledMessageEntity msg : pending) {
            scheduleWakeUpCall(msg.getScheduleTime());
        }
    }

    public void scheduleWakeUpCall(LocalDateTime scheduleTime) {
        Date startTime = Date.from(scheduleTime.atZone(ZoneId.systemDefault()).toInstant());
        taskScheduler.schedule(() -> {
            logger.info("Wake Up Call! Checking for messages scheduled at {}", scheduleTime);
            reactiveScheduleService.processBatch();
        }, startTime);
    }
}
