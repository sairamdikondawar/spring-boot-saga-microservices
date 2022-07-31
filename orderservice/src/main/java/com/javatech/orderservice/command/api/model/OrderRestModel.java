package com.javatech.orderservice.command.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderRestModel {

    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
}
