package com.javatech.commonservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderShippedEvent {

    private String shipmentId;
    private String orderId;
    private String shipmentStatus;
}
