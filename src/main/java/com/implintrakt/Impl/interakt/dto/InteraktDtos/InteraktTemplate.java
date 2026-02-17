package com.implintrakt.Impl.interakt.dto.InteraktDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InteraktTemplate {

    @JsonProperty("name")
    private String name;

    @JsonProperty("languageCode")
    private String languageCode;

    @JsonProperty("bodyValues")
    private List<String> bodyValues;

    @JsonProperty("headerValues")
    private List<String> headerValues;

    @JsonProperty("buttonValues")
    private Map<String, List<String>> buttonValues;
}