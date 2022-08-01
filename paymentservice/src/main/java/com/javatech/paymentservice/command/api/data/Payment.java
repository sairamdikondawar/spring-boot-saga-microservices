package com.javatech.paymentservice.command.api.data;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Data
@Entity
@Builder
@Table(name = "Payment")
public class Payment {

    @Id
    private String paymentId;
    private String orderId;
    private Date timeStamp;
    private String paymentStatus;
}
