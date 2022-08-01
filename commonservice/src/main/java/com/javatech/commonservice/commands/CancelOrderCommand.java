package com.javatech.commonservice.commands;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class CancelOrderCommand {

    @TargetAggregateIdentifier
    private String orderId;
    private String orderStatus="CANCELLED";
}
