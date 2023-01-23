package com.kezbek.transaction.external;

import com.alibaba.fastjson.JSON;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.kezbek.transaction.model.request.AuthorizationRequest;
import com.kezbek.transaction.model.response.SessionResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CognitoAuthorization {

    @Autowired
    private AWSCognitoIdentityProvider amazonCognitoProvider;

    @Value("${integration.aws-cognito.clientid}")
    private String clientId;

    @Value("${integration.aws-cognito.secret}")
    private String secret;

    private String secretHash(String username) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        mac.update(username.getBytes(StandardCharsets.UTF_8));
        byte[] raw = mac.doFinal(clientId.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(raw);
    }

    public SessionResponse execute(AuthorizationRequest request) {
        String secretHash = secretHash(request.getCompanyCode());
        InitiateAuthRequest initiateAuthRequest = new InitiateAuthRequest();
        Map<String, String> authMap = new HashMap<>();
        log.info("decrypted result request [{}]", JSON.toJSONString(request));
        authMap.put("USERNAME", request.getCompanyCode());
        authMap.put("PASSWORD", request.getPassword());
        authMap.put("SECRET_HASH", secretHash);
        initiateAuthRequest.setAuthFlow(AuthFlowType.USER_PASSWORD_AUTH);
        initiateAuthRequest.setClientId(clientId);
        initiateAuthRequest.setAuthParameters(authMap);
        InitiateAuthResult result = this.amazonCognitoProvider.initiateAuth(initiateAuthRequest);
        return SessionResponse.builder()
                .accessToken(result.getAuthenticationResult().getAccessToken())
                .token(result.getAuthenticationResult().getIdToken())
                .refreshToken(result.getAuthenticationResult().getRefreshToken())
                .build();
    }
}
