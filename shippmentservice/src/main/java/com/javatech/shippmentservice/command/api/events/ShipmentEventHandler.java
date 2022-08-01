package com.javatech.shippmentservice.command.api.events;

import com.javatech.commonservice.events.OrderShippedEvent;
import com.javatech.shippmentservice.command.api.data.Shipment;
import com.javatech.shippmentservice.command.api.data.ShipmentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipmentEventHandler {

    private ShipmentRepository shipmentRepository;

    @Autowired
    public ShipmentEventHandler(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        Shipment shipment
                =Shipment.builder().shipmentId(event.getShipmentId())
                .orderId(event.getOrderId())
                .shipmentStatus(event.getShipmentStatus())
                .build();
        shipmentRepository.save(shipment);
    }
}
