package com.implintrakt.Impl.interakt.dto.InteraktTemplateDto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InteraktTemplateSaveRequestDto {
    private String contactStatus;
    private LocalDateTime scheduledTime;
    private String templateName;
    private String languageCode;
    private List<String> headerValue;
    private List<String> bodyValue;
    private Map<String, List<String>> buttonValue;
}
