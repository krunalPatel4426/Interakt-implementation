package com.implintrakt.Impl.interakt.service.contactDetailService.impl;

import com.implintrakt.Impl.interakt.dto.AddContactDetailDto.AddContactDetailDto;
import com.implintrakt.Impl.interakt.dto.ApiResponse.ApiResponse;
import com.implintrakt.Impl.interakt.model.ContactDetailEntity;
import com.implintrakt.Impl.interakt.repository.ContactDetailRepository;
import com.implintrakt.Impl.interakt.service.contactDetailService.ContactDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactDetailServiceImpl implements ContactDetailService {

    Logger logger = LoggerFactory.getLogger(ContactDetailServiceImpl.class);


    @Autowired
    private ContactDetailRepository contactDetailRepository;

    @Override
    public ResponseEntity<ApiResponse> addContact(List<AddContactDetailDto> contactDetailDtos) {
        try{
            List<ContactDetailEntity> contactDetailEntityList = contactDetailDtos.stream()
                    .map(data -> {
                        ContactDetailEntity contactDetailEntity = new ContactDetailEntity();
                        contactDetailEntity.setPhoneNumber(data.getPhoneNumber());
                        contactDetailEntity.setStatus(data.getStatus());
                        return contactDetailEntity;
                    }).toList();
            contactDetailRepository.saveAll(contactDetailEntityList);
            return ResponseEntity.ok(new ApiResponse("true", "Data Stored", null));
        }catch (Exception e){
            logger.error("Error: {} ", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again later.");
        }
    }
}
