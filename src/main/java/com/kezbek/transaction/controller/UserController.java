package com.kezbek.transaction.controller;

import com.kezbek.transaction.model.request.PartnerRequest;
import com.kezbek.transaction.service.PartnerService;
import com.kezbek.transaction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/downgrade")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping(value = "/user")
    public String addPartner(){
        userService.downGradeUserTier();
        return "success";
    }
}
