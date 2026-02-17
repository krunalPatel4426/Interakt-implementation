package com.implintrakt.Impl.interakt.service.authService.Impl;

import com.implintrakt.Impl.interakt.component.Clients.InteraktClient;
import com.implintrakt.Impl.interakt.dto.ApiResponse.ApiResponse;
import com.implintrakt.Impl.interakt.dto.AuthDto.LoginRequestDto;
import com.implintrakt.Impl.interakt.service.authService.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private InteraktClient interaktClient;

    @Override
    public ResponseEntity<ApiResponse> login(LoginRequestDto loginRequestDto) {
        try{
            Object obj = interaktClient.login(loginRequestDto);
            System.out.println(obj.toString());
            return ResponseEntity.ok(new ApiResponse("true", "login successfull.", obj));
        }catch (Exception e){
            e.getMessage();
            throw new RuntimeException("Something went wrong");
        }
    }
}
