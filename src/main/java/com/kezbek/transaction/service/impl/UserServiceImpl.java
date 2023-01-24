package com.kezbek.transaction.service.impl;

import com.kezbek.transaction.repository.UserTierRepository;
import com.kezbek.transaction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserTierRepository userTierRepository;
    @Scheduled(cron = "00 00 01 * * ?")
    public void downGradeUserTier(){
        userTierRepository.downGradeTire();
    }
}
