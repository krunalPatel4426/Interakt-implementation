package com.implintrakt.Impl.interakt.controller.REST.ContactDetailController;


import com.implintrakt.Impl.interakt.dto.AddContactDetailDto.AddContactDetailDto;
import com.implintrakt.Impl.interakt.dto.ApiRequestDto;
import com.implintrakt.Impl.interakt.dto.ApiResponse.ApiResponse;
import com.implintrakt.Impl.interakt.service.contactDetailService.ContactDetailService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactDetailController {

    @Autowired
    private ContactDetailService contactDetailService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addContact(@RequestBody List<AddContactDetailDto> contactDetailDtos){
        return contactDetailService.addContact(contactDetailDtos);
    }
}
