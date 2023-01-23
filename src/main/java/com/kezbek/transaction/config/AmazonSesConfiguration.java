package com.kezbek.transaction.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemailv2.AmazonSimpleEmailServiceV2;
import com.amazonaws.services.simpleemailv2.AmazonSimpleEmailServiceV2ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonSesConfiguration {

    @Value("${integration.aws-ses.region}")
    private String region;

    @Value("${integration.aws-ses.access-key}")
    private String accessKey;

    @Value("${integration.aws-ses.secret-key}")
    private String secretKey;

    @Bean
    public AmazonSimpleEmailServiceV2 amazonSesClientV2() {
        AWSCredentialsProvider credentialProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                this.accessKey, this.secretKey));
        AmazonSimpleEmailServiceV2ClientBuilder clientBuilder = AmazonSimpleEmailServiceV2ClientBuilder.standard();
        clientBuilder.setRegion(region);
        clientBuilder.setCredentials(credentialProvider);
        return clientBuilder.build();
    }
}
