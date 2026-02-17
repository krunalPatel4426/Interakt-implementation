package com.implintrakt.Impl.interakt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequestDto {
    private String countryCode;
    private String phoneNumber;
}
