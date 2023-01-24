package com.kezbek.transaction.service.impl;

import com.kezbek.transaction.entity.Partner;
import com.kezbek.transaction.model.request.PartnerRequest;
import com.kezbek.transaction.model.response.CommonResponse;
import com.kezbek.transaction.repository.PartnerRepository;
import com.kezbek.transaction.service.PartnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PartnerServiceImpl implements PartnerService {

    @Autowired
    PartnerRepository partnerRepository;

    @Override
    public ResponseEntity add(PartnerRequest partnerInput) {
        try{
            Partner partner = Partner.builder()
                    .partnerCode(partnerInput.getPartnerCode())
                    .name(partnerInput.getName())
                    .description(partnerInput.getDescription())
                    .category(partnerInput.getCategory())
                    .build();
            partnerRepository.save(partner);
            return ResponseEntity.ok(new CommonResponse());
        }catch (Exception e){
            log.error(e.getMessage());
            return new  ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }
}
