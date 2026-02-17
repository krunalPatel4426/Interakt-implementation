package com.implintrakt.Impl.interakt.config.globalException;

import com.implintrakt.Impl.interakt.config.globalException.customException.InteraktInvalidArgException;
import com.implintrakt.Impl.interakt.config.globalException.customException.InterkatException;
import com.implintrakt.Impl.interakt.dto.ApiResponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception e){
        return new ResponseEntity<>(new ApiResponse("false", e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleException(RuntimeException e){
        return new ResponseEntity<>(new ApiResponse("false", e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InterkatException.class)
    public ResponseEntity<ApiResponse> handleException(InterkatException e){
        return new ResponseEntity<>(new ApiResponse("false", e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InteraktInvalidArgException.class)
    public ResponseEntity<ApiResponse> handleException(InteraktInvalidArgException e){
        return new ResponseEntity<>(new ApiResponse("false", e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
