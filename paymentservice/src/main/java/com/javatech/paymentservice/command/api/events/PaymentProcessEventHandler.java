package com.javatech.paymentservice.command.api.events;

import com.javatech.commonservice.events.PaymentProcessedEvent;
import com.javatech.paymentservice.command.api.data.Payment;
import com.javatech.paymentservice.command.api.data.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PaymentProcessEventHandler {

    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentProcessEventHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent)
    {
        Payment payment=Payment.builder()
                .paymentId(paymentProcessedEvent.getPaymentId())
                .paymentStatus("COMPLETED")
                .timeStamp(new Date())
                .orderId(paymentProcessedEvent.getOrderId())
                .build();
        paymentRepository.save(payment);
    }
}
