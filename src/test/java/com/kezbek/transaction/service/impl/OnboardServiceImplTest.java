package com.kezbek.transaction.service.impl;

import com.kezbek.transaction.external.CognitoAuthorization;
import com.kezbek.transaction.external.CognitoInfo;
import com.kezbek.transaction.model.request.AuthorizationRequest;
import com.kezbek.transaction.model.request.ContextRequest;
import com.kezbek.transaction.model.response.SessionResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
@DisplayName("Onboard Service Test")
public class OnboardServiceImplTest {
    OnboardServiceImpl onboardService;
    @Mock
    CognitoAuthorization cognitoAuthorization;
    @Mock
    CognitoInfo cognitoInfo;

    @BeforeEach
    void init() {
        this.onboardService = new OnboardServiceImpl();
        this.onboardService.cognitoInfo = cognitoInfo;
        this.onboardService.cognitoAuthorization = cognitoAuthorization;
    }

    @Test
    @DisplayName("Success On Board")
    void testSuccess() {
        //arrange
        AuthorizationRequest request = AuthorizationRequest.builder().companyCode("tokopedia").password("password").build();
        SessionResponse response = SessionResponse.builder().subId("1").accessToken("abc").company("tokopedia").token("123").companyCode("tokopedia").refreshToken("123").build();
        Mockito.when(cognitoAuthorization.execute(request)).thenReturn(response);
        Mockito.when(cognitoInfo.execute(ContextRequest.builder()
                .token(response.getToken())
                .build())).thenReturn(SessionResponse.builder().subId("1").accessToken("abc").company("tokopedia").token("123").companyCode("tokopedia").refreshToken("123").build());
        //act
        SessionResponse sessionResponse = onboardService.authorization(request);
        //assert
        Assertions.assertNotNull(sessionResponse);
    }
}
