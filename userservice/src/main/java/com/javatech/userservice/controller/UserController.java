package com.javatech.userservice.controller;

import com.javatech.commonservice.model.User;
import com.javatech.commonservice.queries.GetUserPaymentDetailsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private transient QueryGateway queryGateway;

    @GetMapping("/{userId}")
    public User getUserPaymentDetails(@PathVariable String userId)
    {

        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery=new GetUserPaymentDetailsQuery(userId);
        User user=queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();

        return user;
    }
}
