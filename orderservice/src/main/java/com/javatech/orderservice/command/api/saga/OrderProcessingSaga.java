package com.javatech.orderservice.command.api.saga;

import com.javatech.commonservice.commands.*;
import com.javatech.commonservice.events.*;
import com.javatech.commonservice.model.PaymentDetails;
import com.javatech.commonservice.model.User;
import com.javatech.commonservice.queries.GetOrderPaymentDetailsQuery;
import com.javatech.commonservice.queries.GetUserPaymentDetailsQuery;
import com.javatech.orderservice.command.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
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
    private transient QueryGateway queryGateway;


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

            ValidatePaymentCommand validatePaymentCommand
                    = ValidatePaymentCommand
                    .builder()
                    .paymentId(UUID.randomUUID().toString())
                    .cardDetails(user.getCardDetails())
                    .orderId(event.getOrderId())
                    .build();
            commandGateway.sendAndWait(validatePaymentCommand);
        } catch (Exception e) {
            log.error(e.getMessage());
            cancelOrderCommand(event.getOrderId());
        }

    }

    private void cancelOrderCommand(String orderId) {
        CancelOrderCommand command = new CancelOrderCommand(orderId);
        commandGateway.send(command);
    }


    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handler(OrderCancelledEvent event) {
        log.info("OrderCancelledEvent Order Id : {} ", event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handler(PaymentCancelledEvent event) {
        log.info("PaymentCancelledEvent Order Id : {} ", event.getOrderId());
        cancelOrderCommand(event.getOrderId());
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
            cancelPaymentCommand(paymentProcessedEvent);
        }
    }

    private void cancelPaymentCommand(PaymentProcessedEvent event) {
        CancelPaymentCommand command
                = new CancelPaymentCommand(event.getPaymentId(), event.getOrderId());
        commandGateway.send(command);

    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handler(OrderShippedEvent event) {
        log.info("Order Shipment Event Order Id : {} ", event.getOrderId());
        try {
            if (true)
                throw new Exception();
            CompleteOrderCommand command
                    = CompleteOrderCommand
                    .builder()
                    .orderId(event.getOrderId())
                    .orderStatus("APPROVED")
                    .build();
            commandGateway.send(command);

        } catch (Exception e) {
            log.error(e.getMessage());
            cancelShipmentCommand(event);
        }
    }

    private void cancelShipmentCommand(OrderShippedEvent event) {
        CancelShipmentCommand command
                = new CancelShipmentCommand(event.getShipmentId(), event.getOrderId());
        commandGateway.send(command);

    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handler(ShipmentCancelledEvent event) {
        log.info("ShipmentCancelledEvent Order Id : {}, Shipement Id : {} ", event.getOrderId(), event.getShipmentId());


        GetOrderPaymentDetailsQuery getOrderPaymentDetailsQuery = new GetOrderPaymentDetailsQuery(event.getOrderId());
        PaymentDetails paymentDetails = null;

        paymentDetails = queryGateway.query(getOrderPaymentDetailsQuery, ResponseTypes.instanceOf(PaymentDetails.class)).join();

        PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent();
        paymentProcessedEvent.setOrderId(event.getOrderId());
        paymentProcessedEvent.setPaymentId(paymentDetails.getPaymentId());

        cancelPaymentCommand(paymentProcessedEvent);

    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
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