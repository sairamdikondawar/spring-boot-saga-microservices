package com.javatech.orderservice.command.api.saga;

import com.javatech.commonservice.commands.CompleteOrderCommand;
import com.javatech.commonservice.commands.ShipOrderCommand;
import com.javatech.commonservice.commands.ValidatePaymentCommand;
import com.javatech.commonservice.events.OrderCompletedEvent;
import com.javatech.commonservice.events.OrderShippedEvent;
import com.javatech.commonservice.events.PaymentProcessedEvent;
import com.javatech.commonservice.model.User;
import com.javatech.commonservice.queries.GetUserPaymentDetailsQuery;
import com.javatech.orderservice.command.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
public class OrderProcessingSaga {


    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private  transient QueryGateway queryGateway;


    public OrderProcessingSaga() {

    }



    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent in Saga for Order Id : {} ", event.getOrderId());
        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery = new GetUserPaymentDetailsQuery(event.getUserId());
        User user = null;
        try {
            user = queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception e) {
            log.error(e.getMessage());
            //TODO Start the compensating transaction
        }
        ValidatePaymentCommand validatePaymentCommand
                = ValidatePaymentCommand
                .builder()
                .paymentId(UUID.randomUUID().toString())
                .cardDetails(user.getCardDetails())
                .orderId(event.getOrderId())
                .build();
        commandGateway.sendAndWait(validatePaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handler(PaymentProcessedEvent paymentProcessedEvent) {
        log.info("Payment Processing Event Order Id : {} ", paymentProcessedEvent.getOrderId());
        try {
            ShipOrderCommand shipOrderCommand
                    = ShipOrderCommand
                    .builder()
                    .shipmentId(UUID.randomUUID().toString())
                    .orderId(paymentProcessedEvent.getOrderId())
                    .build();
            commandGateway.send(shipOrderCommand);

        } catch (Exception e) {
            log.error(e.getMessage());
            //TODO Start the compensating transaction
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handler(OrderShippedEvent event) {
        log.info("Order Shipment Event Order Id : {} ", event.getOrderId());
        try {
            CompleteOrderCommand command
                    = CompleteOrderCommand
                    .builder()
                    .orderId(event.getOrderId())
                    .orderStatus("APPROVED")
                    .build();
            commandGateway.send(command);

        } catch (Exception e) {
            log.error(e.getMessage());
            //TODO Start the compensating transaction
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handler(OrderCompletedEvent event) {
        log.info("Order Complete Event Order Id : {} ", event.getOrderId());
        try {
            //TODO Complete the below Send Invoice Services
//            SendInvoiceCommand command
//                    = SendInvoiceCommand
//                    .builder()
//                    .orderId(event.getOrderId())
//                    .invoiceId(UUID.randomUUID().toString())
//                    .build();
//            commandGateway.sendAndWait(command);

        } catch (Exception e) {
            log.error(e.getMessage());
            //TODO Start the compensating transaction
        }
    }


}