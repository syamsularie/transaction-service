package com.kezbek.transaction.service;

import com.kezbek.transaction.model.request.PartnerRequest;
import org.springframework.http.ResponseEntity;

public interface PartnerService {
    ResponseEntity add(PartnerRequest partnerInput);
}
