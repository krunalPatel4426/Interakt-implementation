package com.implintrakt.Impl.interakt.service.scheduleService;

import com.implintrakt.Impl.interakt.dto.ApiResponse.ApiResponse;
import com.implintrakt.Impl.interakt.dto.InteraktTemplateDto.InteraktTemplateRequestDto;
import com.implintrakt.Impl.interakt.dto.InteraktTemplateDto.InteraktTemplateSaveRequestDto;
import org.springframework.http.ResponseEntity;

public interface ScheduleService {
    ResponseEntity<ApiResponse> testMessage(InteraktTemplateRequestDto requestDto);

    ResponseEntity<ApiResponse> saveMessage(InteraktTemplateSaveRequestDto requestDto);
}
