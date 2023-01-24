package com.kezbek.transaction.router;

import com.alibaba.fastjson.JSON;
import com.kezbek.transaction.model.response.UserTransactionResponse;
import com.kezbek.transaction.service.impl.SendEmailNotificationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.component.aws2.sqs.Sqs2Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.camel.builder.RouteBuilder;

@Component
@Slf4j
public class QueueProcessTransactionRouter extends RouteBuilder {
    @Value("${integration.aws-sqs.access-key}")
    String accessKey;

    @Value("${integration.aws-sqs.secret-key}")
    String secretKey;

    @Value("${integration.aws-sqs.queue-name}")
    String queue;

    @Value("${integration.aws-sqs.region}")
    String region;

    @Autowired
    SendEmailNotificationServiceImpl sendEmailNotificationService;

    @Override
    public void configure() {
        String address = "aws2-sqs://".concat(queue).concat("?accessKey=")
                .concat(accessKey).concat("&secretKey=RAW(".concat(secretKey)
                        .concat(")&region=".concat(region)));
        log.info("Transaction - transaction consumer is running [{}]", address);
        from(address)
                .setHeader(Sqs2Constants.SQS_OPERATION, constant("listQueues"))
                .process(exchange -> {
                    UserTransactionResponse userTransactionResponse = JSON.parseObject(String.valueOf(exchange.getMessage().getBody()), UserTransactionResponse.class);
                    log.info("user transaction consumer exchange [{}]", userTransactionResponse);
//                    sendEmailNotificationService.execute(userTransactionResponse);
                });
    }
}
