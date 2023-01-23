package com.kezbek.transaction.controller;

import com.kezbek.transaction.model.request.PartnerRequest;
import com.kezbek.transaction.model.response.UserTransactionResponse;
import com.kezbek.transaction.service.PartnerService;
import com.kezbek.transaction.service.impl.SendEmailNotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    SendEmailNotificationServiceImpl emailService;

    @GetMapping(value = "/send")
    public ResponseEntity send(){
        emailService.execute(UserTransactionResponse.builder().build());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
