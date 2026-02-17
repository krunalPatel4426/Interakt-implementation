package com.implintrakt.Impl.interakt.service.authService;

import com.implintrakt.Impl.interakt.dto.ApiResponse.ApiResponse;
import com.implintrakt.Impl.interakt.dto.AuthDto.LoginRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse> login(LoginRequestDto loginRequestDto);
}
