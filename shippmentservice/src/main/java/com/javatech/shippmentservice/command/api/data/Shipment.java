package com.javatech.shippmentservice.command.api.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "shipment")
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {
    @Id
    private String shipmentId;
    private String shipmentStatus;
    private String orderId;
}
