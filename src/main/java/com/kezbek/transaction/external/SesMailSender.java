package com.kezbek.transaction.external;

import com.amazonaws.services.simpleemailv2.AmazonSimpleEmailServiceV2;
import com.amazonaws.services.simpleemailv2.model.*;
import com.kezbek.transaction.model.request.EmailRequest;
import com.kezbek.transaction.model.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

@Component
@Slf4j
public class SesMailSender implements EnvironmentAdaptor<EmailRequest, CommonResponse> {

    @Autowired
    AmazonSimpleEmailServiceV2 amazonSesClientV2;

    @Override
    public CommonResponse execute(EmailRequest request) {
        SendEmailRequest sendRequest = new SendEmailRequest();
        EmailContent content = new EmailContent();
        Message message = new Message();
        Body body = new Body();
        body.setHtml(new Content().withCharset("UTF-8").withData(request.getContent()));
        message.setBody(body);
        message.setSubject(new Content().withCharset("UTF-8").withData(request.getSubject()));
        content.setSimple(message);
        sendRequest.setDestination(new Destination()
                .withToAddresses(Arrays.asList(request.getDestinations()))
                .withCcAddresses(ObjectUtils.isEmpty(request.getCc()) ? null : Arrays.asList(request.getCc()))
                .withBccAddresses(ObjectUtils.isEmpty(request.getBcc()) ? null : Arrays.asList(request.getBcc()))
        );
        sendRequest.setFromEmailAddress(request.getFrom());
        sendRequest.setContent(content);
        log.info(String.valueOf(sendRequest));
        amazonSesClientV2.sendEmail(sendRequest);
        return new CommonResponse();
    }
}
