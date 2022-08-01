package com.javatech.shippmentservice.command.api.aggregate;

import com.javatech.commonservice.commands.ShipOrderCommand;
import com.javatech.commonservice.events.OrderShippedEvent;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Data
public class ShipmentAggregate {

    @AggregateIdentifier
    private String shipmentId;
    private String orderId;
    private String shipmentStatus;

    private ShipmentAggregate() {

    }

    @CommandHandler
    private ShipmentAggregate(ShipOrderCommand shipOrderCommand) {
        //validate the shipOrderCommand
        //Publish the Order Shipment Event
        OrderShippedEvent orderShippedEvent
                = OrderShippedEvent
                .builder()
                .shipmentId(shipOrderCommand.getShipmentId())
                .orderId(shipOrderCommand.getOrderId())
                .shipmentStatus("COMPLETED").build();
        AggregateLifecycle.apply(orderShippedEvent);
    }

    @EventSourcingHandler
    public void on(OrderShippedEvent event) {
        this.shipmentId = event.getShipmentId();
        this.shipmentStatus = event.getShipmentStatus();
        this.orderId = event.getOrderId();
    }

}
