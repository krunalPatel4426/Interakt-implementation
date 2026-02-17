package com.implintrakt.Impl.interakt.service.messageService;

import com.implintrakt.Impl.interakt.dto.ApiRequestDto;
import com.implintrakt.Impl.interakt.dto.ApiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface MessageService {

    ResponseEntity<ApiResponse> sendMessage(ApiRequestDto requestDto);
}
