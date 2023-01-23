package com.kezbek.transaction.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonSqsConfiguration {

    @Value("${integration.aws-sqs.access-key}")
    private String accessKey;

    @Value("${integration.aws-sqs.secret-key}")
    private String secretKey;

    @Value("${integration.aws-sqs.region}")
    private String region;

    @Bean
    public AmazonSQS amazonSqsClient() {
        AWSCredentials credentials = new BasicAWSCredentials(
                this.accessKey,
                this.secretKey);
        return AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }
}
