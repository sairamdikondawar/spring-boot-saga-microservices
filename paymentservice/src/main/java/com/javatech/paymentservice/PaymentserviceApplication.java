package com.javatech.paymentservice;

import com.javatech.commonservice.config.AxonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ AxonConfig.class })
public class PaymentserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentserviceApplication.class, args);
	}

}
