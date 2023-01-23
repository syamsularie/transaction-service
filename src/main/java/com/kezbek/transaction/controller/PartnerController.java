package com.kezbek.transaction.controller;

import com.kezbek.transaction.model.request.PartnerRequest;
import com.kezbek.transaction.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/partner")
public class PartnerController {
    @Autowired
    PartnerService partnerService;

    @PostMapping(value = "/v1/addPartner")
    public ResponseEntity addPartner(@RequestBody @Valid PartnerRequest partnerRequest){
        return partnerService.add(partnerRequest);
    }
}
