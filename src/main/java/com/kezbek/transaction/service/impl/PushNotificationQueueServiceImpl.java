package com.kezbek.transaction.service.impl;

import com.kezbek.transaction.external.SqsSender;
import com.kezbek.transaction.model.response.UserTransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationQueueServiceImpl {
    @Autowired
    SqsSender<UserTransactionResponse> sqsSender;

    @Value("${integration.aws-sqs.queue-name}")
    String queueName;
    @Value("${integration.aws-sqs.queue-url}")
    String queueUrl;

    public void execute(UserTransactionResponse input) {
        sqsSender.setQueue(queueName);
        sqsSender.setQueueUrl(queueUrl);
        sqsSender.execute(input);
    }
}
