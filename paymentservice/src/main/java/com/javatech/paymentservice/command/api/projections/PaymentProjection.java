package com.javatech.paymentservice.command.api.projections;

import com.javatech.commonservice.model.PaymentDetails;
import com.javatech.commonservice.queries.GetOrderPaymentDetailsQuery;
import com.javatech.paymentservice.command.api.data.Payment;
import com.javatech.paymentservice.command.api.data.PaymentRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class PaymentProjection {

    private PaymentRepository paymentRepository;


    public PaymentProjection(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @QueryHandler
    public PaymentDetails getPaymentDetails(GetOrderPaymentDetailsQuery query) {
        Payment payment = paymentRepository.findByOrderId(query.getOrderId()).get();
       return PaymentDetails
               .builder()
               .paymentId(payment.getPaymentId())
               .orderId(payment.getOrderId())
               .paymentStatus(payment.getPaymentStatus())
               .build();
    }
}
