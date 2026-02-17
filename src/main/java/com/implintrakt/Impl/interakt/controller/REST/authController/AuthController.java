package com.implintrakt.Impl.interakt.controller.REST.authController;

import com.implintrakt.Impl.interakt.dto.ApiResponse.ApiResponse;
import com.implintrakt.Impl.interakt.dto.AuthDto.LoginRequestDto;
import com.implintrakt.Impl.interakt.service.authService.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }
}
