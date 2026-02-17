package com.implintrakt.Impl.interakt.dto.InteraktDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InteraktResponseDto {
    @JsonProperty("result")
    private boolean result;

    @JsonProperty("message")
    private String message;

    @JsonProperty("id")
    private String id; // The Message ID
}
