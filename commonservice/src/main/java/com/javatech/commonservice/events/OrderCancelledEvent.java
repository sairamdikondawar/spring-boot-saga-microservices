package com.javatech.commonservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OrderCancelledEvent {

    private String orderId;
    private String orderStatus;
}
