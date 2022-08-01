package com.javatech.commonservice.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendInvoiceCommand {

    @TargetAggregateIdentifier
    private String orderId;
    private String invoiceId;

}
