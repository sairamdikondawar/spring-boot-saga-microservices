package com.javatech.commonservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PaymentDetails {
    private String paymentId;
    private String orderId;
    private Date timeStamp;
    private String paymentStatus;
}
