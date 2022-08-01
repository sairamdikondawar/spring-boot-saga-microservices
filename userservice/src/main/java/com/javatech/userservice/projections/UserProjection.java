package com.javatech.userservice.projections;

import com.javatech.commonservice.model.CardDetails;
import com.javatech.commonservice.model.User;
import com.javatech.commonservice.queries.GetUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserProjection {

    @QueryHandler
    public User getUserPaymentDetails(GetUserPaymentDetailsQuery query) {

        CardDetails cardDetails = CardDetails.builder().name("Sai Ram").cardNumber("123433").cvv(122).validUntilDate("22").validUntilMonth("10").build();
      //TODO Return the details from the database
        return User.builder().userId(query.getUserId()).cardDetails(cardDetails).build();
    }
}
