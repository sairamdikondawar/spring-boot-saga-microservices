package com.javatech.paymentservice.command.api.events;

import com.javatech.commonservice.events.PaymentCancelledEvent;
import com.javatech.commonservice.events.PaymentProcessedEvent;
import com.javatech.paymentservice.command.api.data.Payment;
import com.javatech.paymentservice.command.api.data.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PaymentProcessEventHandler {

    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentProcessEventHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        Payment payment = Payment.builder()
                .paymentId(paymentProcessedEvent.getPaymentId())
                .paymentStatus("COMPLETED")
                .timeStamp(new Date())
                .orderId(paymentProcessedEvent.getOrderId())
                .build();
        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PaymentCancelledEvent event) {
        Payment payment =
                paymentRepository.findById(event.getPaymentId()).get();
        payment.setPaymentStatus(event.getPaymentStatus());
        paymentRepository.save(payment);
    }
}
