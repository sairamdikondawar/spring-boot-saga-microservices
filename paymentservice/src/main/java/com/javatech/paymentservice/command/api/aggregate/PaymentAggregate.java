package com.javatech.paymentservice.command.api.aggregate;

import com.javatech.commonservice.commands.CancelPaymentCommand;
import com.javatech.commonservice.commands.ValidatePaymentCommand;
import com.javatech.commonservice.events.PaymentCancelledEvent;
import com.javatech.commonservice.events.PaymentProcessedEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Data
//@Builder
@Aggregate
@Slf4j
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private String paymentStatus;

    public PaymentAggregate() {
    }

    @CommandHandler
    public PaymentAggregate(ValidatePaymentCommand validatePaymentCommand) {
        //Validate the payment details;

        // Publish the payment details
        log.info("Validate Payment Command Order Id : {} , Payment Id : {} ", validatePaymentCommand.getOrderId(), validatePaymentCommand.getPaymentId());
        PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent
                (validatePaymentCommand.getPaymentId(), validatePaymentCommand.getOrderId());
        AggregateLifecycle.apply(paymentProcessedEvent);
        log.info("PaymentProcessedEvent applied");
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event) {
        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
    }

    @CommandHandler
    public void handle(CancelPaymentCommand command) {
        //Validate the payment details;

        // Publish the payment details
        log.info("CancelPaymentCommand Order Id : {} , Payment Id : {} ", command.getOrderId(), command.getPaymentId());

        PaymentCancelledEvent paymentCancelledEvent = new PaymentCancelledEvent();

        BeanUtils.copyProperties(command,paymentCancelledEvent);

        AggregateLifecycle.apply(paymentCancelledEvent);
        log.info("CancelPaymentCommand applied");
    }

    @EventSourcingHandler
    public void on(PaymentCancelledEvent event) {
//        this.paymentId = event.getPaymentId();
//        this.orderId = event.getOrderId();
        this.paymentStatus = event.getPaymentStatus();
    }


}
