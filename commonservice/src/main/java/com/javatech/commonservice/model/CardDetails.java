package com.javatech.commonservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDetails {
    private String cardNumber;
    private String name;
    private String validUntilMonth;
    private String validUntilDate;
    private Integer cvv;
}
