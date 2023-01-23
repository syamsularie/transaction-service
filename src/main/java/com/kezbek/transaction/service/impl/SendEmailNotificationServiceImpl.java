package com.kezbek.transaction.service.impl;

import com.kezbek.transaction.external.SesMailSender;
import com.kezbek.transaction.model.request.EmailRequest;
import com.kezbek.transaction.model.response.UserTransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendEmailNotificationServiceImpl {
    @Autowired
    SesMailSender sqsMailSender;

    public void execute(UserTransactionResponse userTransactionResponse){
        EmailRequest request = EmailRequest.builder()
                .from("syams.arie@gmail.com")
                .destinations(new String[]{"syams.arie@gmail.com"})
                .content("email masuk")
                .build();
        sqsMailSender.execute(request);
    }
}
