package com.kezbek.transaction.controller;

import com.kezbek.transaction.model.request.TransactionRequest;
import com.kezbek.transaction.model.response.UserTransactionResponse;
import com.kezbek.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping(value = "/v1/addTransaction")
    public ResponseEntity<UserTransactionResponse> addPartner(@RequestBody @Valid TransactionRequest transactionRequest){
        return new ResponseEntity<>(transactionService.getCashback(transactionRequest), HttpStatus.OK);
    }
}
