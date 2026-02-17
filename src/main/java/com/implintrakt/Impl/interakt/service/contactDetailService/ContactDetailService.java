package com.implintrakt.Impl.interakt.service.contactDetailService;

import com.implintrakt.Impl.interakt.dto.AddContactDetailDto.AddContactDetailDto;
import com.implintrakt.Impl.interakt.dto.ApiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContactDetailService {
    ResponseEntity<ApiResponse> addContact(List<AddContactDetailDto> contactDetailDtos);
}
