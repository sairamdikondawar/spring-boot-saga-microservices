package com.javatech.orderservice.command.api.aggregate;

import com.javatech.commonservice.commands.CompleteOrderCommand;
import com.javatech.commonservice.events.OrderCompletedEvent;
import com.javatech.orderservice.command.api.command.CreateOrderCommand;
import com.javatech.orderservice.command.api.events.OrderCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier //TO identi
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
    private String orderStatus;

    public OrderAggregate() {

    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        // Validate This command here.
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties( createOrderCommand,orderCreatedEvent);
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.orderStatus = event.getOrderStatus();
        this.productId = event.getProductId();
        this.addressId = event.getAddressId();
        this.quantity = event.getQuantity();
        this.userId = event.getUserId();
    }

    @CommandHandler
    public void handler(CompleteOrderCommand command) {
        // validate the command
        //Publish Order Completed Event
        OrderCompletedEvent event
                = OrderCompletedEvent
                .builder()
                .orderId(command.getOrderId())
                .orderStatus(command.getOrderStatus())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(OrderCompletedEvent event) {
        this.orderStatus = event.getOrderStatus();
    }
}
