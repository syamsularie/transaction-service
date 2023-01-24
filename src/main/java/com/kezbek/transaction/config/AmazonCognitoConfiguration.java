package com.kezbek.transaction.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AmazonCognitoConfiguration {

    @Value("${integration.aws-cognito.access-key}")
    private String accessKey;

    @Value("${integration.aws-cognito.secret-key}")
    private String secretKey;

    @Value("${integration.aws-cognito.region}")
    private String region;

    @Bean
    public AWSCognitoIdentityProvider amazonCognitoProvider() {
        AWSCredentialsProvider credentialProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                this.accessKey,
                this.secretKey));
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(credentialProvider)
                .withRegion(region)
                .build();
    }
}