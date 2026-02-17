package com.implintrakt.Impl.interakt.dto.WebClientHelper;

import com.implintrakt.Impl.interakt.model.ScheduledMessageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDeliveryResult {
    private ScheduledMessageEntity entity;
    private String status;
    private String error;
}
