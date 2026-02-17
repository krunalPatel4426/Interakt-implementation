package com.implintrakt.Impl.interakt.service.scheduleService.scheduleMessageHelper;

import com.implintrakt.Impl.interakt.config.Constants.Constant;
import com.implintrakt.Impl.interakt.dto.InteraktTemplateDto.InteraktTemplateRequestDto;
import com.implintrakt.Impl.interakt.dto.InteraktDtos.InteraktMessageRequestDto;
import com.implintrakt.Impl.interakt.dto.InteraktDtos.InteraktTemplate;
import com.implintrakt.Impl.interakt.dto.InteraktTemplateDto.InteraktTemplateSaveRequestDto;
import com.implintrakt.Impl.interakt.model.ContactDetailEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleMessageHelper {
    public InteraktMessageRequestDto mapToInteraktPayload(InteraktTemplateRequestDto requestDto) {
        return InteraktMessageRequestDto.builder()
                .countryCode(Constant.COUNTRY_CODE)
                .phoneNumber(requestDto.getPhoneNumber())
                .type("Template")
                .callbackData("Scheduled_Message")
                .template(InteraktTemplate.builder()
                        .name(requestDto.getTemplateName())
                        .languageCode(Constant.LANGUAGE_CODE)
                        .headerValues(requestDto.getHeaderValue())
                        .bodyValues(requestDto.getBodyValue())
                        .buttonValues(requestDto.getButtonValue())
                        .build())
                .build();
    }

    public List<InteraktMessageRequestDto> mapToInteraktPayload(InteraktTemplateSaveRequestDto requestDto, List<ContactDetailEntity> contacts) {
        return contacts.stream()
                .map(data -> {
                    return InteraktMessageRequestDto.builder()
                            .countryCode(Constant.COUNTRY_CODE)
                            .phoneNumber(data.getPhoneNumber())
                            .type("Template")
                            .callbackData("Scheduled_Message")
                            .template(InteraktTemplate.builder()
                                    .name(requestDto.getTemplateName())
                                    .languageCode(Constant.LANGUAGE_CODE)
                                    .headerValues(requestDto.getHeaderValue())
                                    .bodyValues(requestDto.getBodyValue())
                                    .buttonValues(requestDto.getButtonValue())
                                    .build())
                            .build();
                }).toList();
    }
}
