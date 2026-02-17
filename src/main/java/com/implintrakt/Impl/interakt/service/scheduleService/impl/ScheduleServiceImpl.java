package com.implintrakt.Impl.interakt.service.scheduleService.impl;

import com.implintrakt.Impl.interakt.component.Clients.InteraktClient;
import com.implintrakt.Impl.interakt.config.Constants.Constant;
import com.implintrakt.Impl.interakt.config.globalException.customException.InteraktInvalidArgException;
import com.implintrakt.Impl.interakt.dto.ApiResponse.ApiResponse;
import com.implintrakt.Impl.interakt.dto.InteraktDtos.InteraktResponseDto;
import com.implintrakt.Impl.interakt.dto.InteraktTemplateDto.InteraktTemplateRequestDto;
import com.implintrakt.Impl.interakt.dto.InteraktDtos.InteraktMessageRequestDto;
import com.implintrakt.Impl.interakt.dto.InteraktTemplateDto.InteraktTemplateSaveRequestDto;
import com.implintrakt.Impl.interakt.model.ContactDetailEntity;
import com.implintrakt.Impl.interakt.model.ScheduledMessageEntity;
import com.implintrakt.Impl.interakt.repository.ContactDetailRepository;
import com.implintrakt.Impl.interakt.repository.ScheduleMessageRepository;
import com.implintrakt.Impl.interakt.service.dynamicTriggerService.DynamicTriggerService;
import com.implintrakt.Impl.interakt.service.dynamicTriggerService.impl.DynamicTriggerServiceImpl;
import com.implintrakt.Impl.interakt.service.scheduleService.ScheduleService;
import com.implintrakt.Impl.interakt.service.scheduleService.scheduleMessageHelper.ScheduleMessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private InteraktClient interaktClient;

    @Autowired
    private ScheduleMessageRepository scheduleMessageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ScheduleMessageHelper scheduleMessageHelper;

    @Autowired
    private ContactDetailRepository contactDetailRepository;

    @Autowired
    private DynamicTriggerService dynamicTriggerService;

    @Override
    public ResponseEntity<ApiResponse> testMessage(InteraktTemplateRequestDto requestDto) {
        try{
            InteraktMessageRequestDto request = scheduleMessageHelper.mapToInteraktPayload(requestDto);
            InteraktResponseDto responseDto = interaktClient.sendMessage(request);
            if(responseDto.isResult()){
                logger.info(responseDto.getMessage());
                return ResponseEntity.ok(new ApiResponse("true", "Test Message Sent Successfully!", responseDto.getId()));
            }else {
                logger.info(responseDto.getMessage());
                return new ResponseEntity<>(new ApiResponse("false", "Test Message Sent Failed!", responseDto.getId()), HttpStatus.BAD_REQUEST);
            }

        }catch (InteraktInvalidArgException e){
            throw e;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again later.");
        }
    }

    @Override
    public ResponseEntity<ApiResponse> saveMessage(InteraktTemplateSaveRequestDto requestDto) {
        try{
            List<ContactDetailEntity> contacts = contactDetailRepository.findByStatus(requestDto.getContactStatus());

            List<InteraktMessageRequestDto> requestDtoList = scheduleMessageHelper.mapToInteraktPayload(requestDto, contacts);


            List<ScheduledMessageEntity> scheduledMessageList = requestDtoList.stream()
                            .map(data -> {
                                String jsonPayload = objectMapper.writeValueAsString(data);
                                ScheduledMessageEntity scheduledMessage = new ScheduledMessageEntity();
                                scheduledMessage.setPhoneNumber(data.getPhoneNumber());
                                scheduledMessage.setJsonPayload(jsonPayload);
                                scheduledMessage.setScheduleTime(requestDto.getScheduledTime());
                                scheduledMessage.setStatus(Constant.INTERAKT_STATUS_PENDING);
//                                dynamicTriggerService.scheduleWakeUpCall(requestDto.getScheduledTime());
                                return scheduledMessage;
                            }).toList();


            scheduleMessageRepository.saveAll(scheduledMessageList);
            return ResponseEntity.ok(new ApiResponse("true", "Message Scheduled Successfully!", null));

        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again later.");
        }
    }
}
