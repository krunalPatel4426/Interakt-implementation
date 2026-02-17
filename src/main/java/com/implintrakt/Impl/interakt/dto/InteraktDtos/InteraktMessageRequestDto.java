package com.implintrakt.Impl.interakt.dto.InteraktDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InteraktMessageRequestDto {
    @JsonProperty("countryCode")
    private String countryCode;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("callbackData")
    private String callbackData;

    @JsonProperty("type")
    private String type;

    @JsonProperty("template")
    private InteraktTemplate template;
}
