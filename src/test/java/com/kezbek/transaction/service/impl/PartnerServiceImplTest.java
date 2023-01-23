package com.kezbek.transaction.service.impl;

import com.kezbek.transaction.entity.Partner;
import com.kezbek.transaction.model.request.PartnerRequest;
import com.kezbek.transaction.repository.PartnerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Partner Service Test")
public class PartnerServiceImplTest {
    PartnerServiceImpl partnerService;
    @Mock
    PartnerRepository partnerRepository;

    @BeforeEach
    void init() {
        this.partnerService = new PartnerServiceImpl();
        this.partnerService.partnerRepository = partnerRepository;
    }

    @Test
    @DisplayName("Success Add Partner")
    void testSuccess() {
        //arrange
        PartnerRequest partner = PartnerRequest.builder().partnerCode("Tokopedia").name("Tokped").category("test").build();
        when(partnerRepository.save(Mockito.any(Partner.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        //act
        ResponseEntity respone = partnerService.add(partner);
        //assert
        Assertions.assertEquals(HttpStatus.OK, respone.getStatusCode());
    }
}
