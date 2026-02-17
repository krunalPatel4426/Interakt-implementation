package com.implintrakt.Impl.interakt.dto.InteraktTemplateDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InteraktTemplateRequestDto {
    private String phoneNumber;
    private LocalDateTime scheduledTime;
    private String templateName;
    private String languageCode;
    private List<String> headerValue;
    private List<String> bodyValue;
    private Map<String, List<String>> buttonValue;
}
