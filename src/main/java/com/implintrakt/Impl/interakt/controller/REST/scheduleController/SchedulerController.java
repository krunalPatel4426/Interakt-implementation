package com.implintrakt.Impl.interakt.controller.REST.scheduleController;

import com.implintrakt.Impl.interakt.dto.ApiResponse.ApiResponse;
import com.implintrakt.Impl.interakt.dto.InteraktTemplateDto.InteraktTemplateRequestDto;
import com.implintrakt.Impl.interakt.dto.InteraktTemplateDto.InteraktTemplateSaveRequestDto;
import com.implintrakt.Impl.interakt.service.scheduleService.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule")
public class SchedulerController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/test")
    private ResponseEntity<ApiResponse> testMessage(@RequestBody InteraktTemplateRequestDto requestDto) {
        return scheduleService.testMessage(requestDto);
    }

    @PostMapping("/save")
    private ResponseEntity<ApiResponse> saveMessage(@RequestBody InteraktTemplateSaveRequestDto requestDto) {
        return scheduleService.saveMessage(requestDto);
    }

}
