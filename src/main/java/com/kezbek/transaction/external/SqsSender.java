package com.kezbek.transaction.external;

import com.alibaba.fastjson.JSON;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Setter
@Slf4j
public class SqsSender<T> {
    @Autowired
    AmazonSQS amazonSqsClient;
    private String queue;
    private String queueUrl;

    public void execute(T input) {
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put(input.getClass().getName(), new MessageAttributeValue()
                .withStringValue(queue)
                .withDataType("String"));
        SendMessageRequest sendMessageStandardQueue = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(JSON.toJSONString(input))
                .withMessageAttributes(messageAttributes);
        log.info("send message {}", sendMessageStandardQueue);
        amazonSqsClient.sendMessage(sendMessageStandardQueue);
    }

}
