package com.javatech.orderservice.command.api.aggregate;

import com.javatech.orderservice.command.api.command.CreateOrderCommand;
import com.javatech.orderservice.command.api.events.OrderCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
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
        BeanUtils.copyProperties(orderCreatedEvent, createOrderCommand);
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    public  void on(OrderCreatedEvent event)
    {
        this.orderId=event.getOrderId();
        this.orderStatus= event.getOrderId();;
        this.productId=event.getProductId();
        this.addressId=event.getAddressId();
        this.quantity= event.getQuantity();
        this.userId=event.getUserId();
    }
}
