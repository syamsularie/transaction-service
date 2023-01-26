package com.kezbek.transaction.controller;

import com.kezbek.transaction.model.request.AuthorizationRequest;
import com.kezbek.transaction.model.response.SessionResponse;
import com.kezbek.transaction.service.OnboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class OnboardController {

    @Autowired
    private OnboardService onboardService;

    @PostMapping("/authorization")
    ResponseEntity<SessionResponse> authorize(@RequestBody AuthorizationRequest request) {
        return new ResponseEntity<>(onboardService.authorization(request),
                HttpStatus.OK);
    }
}
