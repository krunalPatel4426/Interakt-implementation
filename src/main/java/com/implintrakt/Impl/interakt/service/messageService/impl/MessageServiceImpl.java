package com.implintrakt.Impl.interakt.service.messageService.impl;

import com.implintrakt.Impl.interakt.component.Clients.InteraktClient;
import com.implintrakt.Impl.interakt.config.globalException.customException.InterkatException;
import com.implintrakt.Impl.interakt.dto.ApiRequestDto;
import com.implintrakt.Impl.interakt.dto.ApiResponse.ApiResponse;
import com.implintrakt.Impl.interakt.dto.InteraktDtos.InteraktMessageRequestDto;
import com.implintrakt.Impl.interakt.dto.InteraktDtos.InteraktResponseDto;
import com.implintrakt.Impl.interakt.dto.InteraktDtos.InteraktTemplate;
import com.implintrakt.Impl.interakt.service.messageService.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private InteraktClient interaktClient;

    @Override
    public ResponseEntity<ApiResponse> sendMessage(ApiRequestDto requestDto) {
        try{
            List<String> body = List.of("krunal","krunal","krunal","krunal","krunal");
            Map<String, List<String>> button = Map.of("0", List.of("www.google.com"));
            List<String> header = List.of("krunal");
            InteraktMessageRequestDto interaktMessageRequestDto = InteraktMessageRequestDto.builder()
                    .countryCode(requestDto.getCountryCode())
                    .phoneNumber(requestDto.getPhoneNumber())
                    .callbackData("Message Sent.")
                    .type("Template")
                    .template(InteraktTemplate.builder()
                            .name("subcription_managment")
                            .languageCode("en")
                            .bodyValues(body)
                            .headerValues(header)
                            .buttonValues(button)
                            .build()
                    ).build();
            InteraktResponseDto responseDto = interaktClient.sendMessage(interaktMessageRequestDto);

            if(!responseDto.isResult()){
                throw new InterkatException("Something went wrong while sending the message. Please try again.");
            }
            return new ResponseEntity<>(new ApiResponse("true", "Message sent successfully.", responseDto.getId()), HttpStatus.OK);
        }catch (InterkatException e){
            System.out.println(e.getMessage());
            throw e;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ApiResponse("false", "Something went wrong try again later.", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
