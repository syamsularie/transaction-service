package com.kezbek.transaction.service.impl;

import com.kezbek.transaction.external.CognitoAuthorization;
import com.kezbek.transaction.external.CognitoInfo;
import com.kezbek.transaction.model.request.AuthorizationRequest;
import com.kezbek.transaction.model.request.ContextRequest;
import com.kezbek.transaction.model.response.SessionResponse;
import com.kezbek.transaction.service.OnboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnboardServiceImpl implements OnboardService {

    @Autowired
    CognitoAuthorization cognitoAuthorization;

    @Autowired
    CognitoInfo cognitoInfo;

    @Override
    public SessionResponse authorization(AuthorizationRequest request) {
        SessionResponse authResponse = cognitoAuthorization.execute(request);
        SessionResponse sessionResponse = cognitoInfo.execute(ContextRequest.builder()
                        .token(authResponse.getToken())
                .build());
        sessionResponse.setAccessToken(authResponse.getAccessToken());
        sessionResponse.setToken(authResponse.getToken());
        sessionResponse.setRefreshToken(authResponse.getRefreshToken());
        return sessionResponse;
    }

    @Override
    public SessionResponse userInfo(ContextRequest request) {
        return cognitoInfo.execute(request);
    }

}
