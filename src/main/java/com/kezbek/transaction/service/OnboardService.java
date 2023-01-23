package com.kezbek.transaction.service;

import com.kezbek.transaction.model.request.AuthorizationRequest;
import com.kezbek.transaction.model.request.ContextRequest;
import com.kezbek.transaction.model.response.SessionResponse;

public interface OnboardService {

    SessionResponse authorization(AuthorizationRequest request);
    SessionResponse userInfo(ContextRequest request);

}
