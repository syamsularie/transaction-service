package com.kezbek.transaction.service.impl;

import com.kezbek.transaction.repository.UserTierRepository;
import com.kezbek.transaction.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private UserTierRepository userTierRepository;
//    @Scheduled(cron = "00 00 01 * * ?")
//    @Scheduled(cron = "0 * * * * *")
    public void downGradeUserTier(){
        userTierRepository.downGradeTire();
    }
}
