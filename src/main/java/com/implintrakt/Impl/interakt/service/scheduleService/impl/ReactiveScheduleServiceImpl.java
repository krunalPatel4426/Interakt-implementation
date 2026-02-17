package com.implintrakt.Impl.interakt.service.scheduleService.impl;

import com.implintrakt.Impl.interakt.component.Clients.InteraktClient;
import com.implintrakt.Impl.interakt.config.Constants.Constant;
import com.implintrakt.Impl.interakt.dto.InteraktDtos.InteraktMessageRequestDto;
import com.implintrakt.Impl.interakt.dto.WebClientHelper.MessageDeliveryResult;
import com.implintrakt.Impl.interakt.dto.projection.scheduleMessage.ScheduleMessageProjection;
import com.implintrakt.Impl.interakt.model.ScheduledMessageEntity;
import com.implintrakt.Impl.interakt.repository.ScheduleMessageRepository;
import com.implintrakt.Impl.interakt.service.scheduleService.ReactiveScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReactiveScheduleServiceImpl implements ReactiveScheduleService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ScheduleMessageRepository  scheduleMessageRepository;

    @Autowired
    private InteraktClient interaktClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Scheduled(cron = "0 * * * * *")
    public void processBatch() {
        try{
            List<ScheduleMessageProjection> pendingMessagesPro = scheduleMessageRepository.findReadyMessages(
                    Constant.INTERAKT_STATUS_PENDING, LocalDateTime.now().plusSeconds(2)
            );
            logger.info("Processing pending interakt messages.");
            if(pendingMessagesPro.isEmpty()){
                logger.info("No pending interakt messages.");
                return;
            }

            logger.info("Scheduler: Found {} pending messages.", pendingMessagesPro.size());

            List<Long> idsToLock = pendingMessagesPro.stream()
                    .map(ScheduleMessageProjection::getId)
                    .toList();

            scheduleMessageRepository.updateStatusForIds(idsToLock, Constant.INTERAKT_STATUS_PROCESSING);
            logger.info("Scheduler: Locked {} messages as PROCESSING.", idsToLock.size());

            Flux.fromIterable(pendingMessagesPro)
                    .flatMap(projection -> {
                        ScheduledMessageEntity scheduledMessageEntity = new ScheduledMessageEntity();
                        scheduledMessageEntity.setId(projection.getId());
                        scheduledMessageEntity.setPhoneNumber(projection.getPhoneNumber());
                        scheduledMessageEntity.setJsonPayload(projection.getJsonPayload());
                        scheduledMessageEntity.setScheduleTime(projection.getScheduleTime());
                        scheduledMessageEntity.setStatus(Constant.INTERAKT_STATUS_PROCESSING);
                        try{
                            InteraktMessageRequestDto requestDto = objectMapper.readValue(
                                    scheduledMessageEntity.getJsonPayload(), InteraktMessageRequestDto.class
                            );
                            return interaktClient.sendMessageReactive(scheduledMessageEntity, requestDto);
                        }catch (Exception e){
                            return Mono.just(new MessageDeliveryResult(scheduledMessageEntity, Constant.INTERAKT_STATUS_FAILED, "JSON Error: " + e.getMessage()));
                        }
                    }, 50)
                    .bufferTimeout(50, Duration.ofSeconds(5))
                    .publishOn(Schedulers.boundedElastic())
                    .doOnNext(batchResult -> {
                        List<ScheduledMessageEntity> toUpdate = new ArrayList<>();
                        for (MessageDeliveryResult result : batchResult) {
                            ScheduledMessageEntity entity = result.getEntity();
                            entity.setStatus(result.getStatus());
                            if(result.getError() != null){
                                entity.setErrorMessage(result.getError());
                            }
                            toUpdate.add(entity);
                        }
                        scheduleMessageRepository.saveAll(toUpdate);
                        logger.info("Scheduler: Updated final status for batch of {} messages.", toUpdate.size());
                    })
                    .blockLast();
            logger.info("Scheduler: Batch processing complete.");

        }catch(Exception e){
            logger.error(e.getMessage());
            throw  new RuntimeException(e);
        }
    }
}