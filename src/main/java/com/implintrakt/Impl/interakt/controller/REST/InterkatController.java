package com.implintrakt.Impl.interakt.controller.REST;

import com.implintrakt.Impl.interakt.dto.ApiRequestDto;
import com.implintrakt.Impl.interakt.dto.ApiResponse.ApiResponse;
import com.implintrakt.Impl.interakt.service.messageService.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
public class InterkatController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendMessage(@RequestBody ApiRequestDto requestDto) {
        return messageService.sendMessage(requestDto);
    }
}
