package com.kezbek.transaction.service.impl;

import com.kezbek.transaction.entity.*;
import com.kezbek.transaction.external.TopUpEmoney;
import com.kezbek.transaction.feign.KezbekDroolsClientService;
import com.kezbek.transaction.model.request.PotentialCashbackRequest;
import com.kezbek.transaction.model.request.TransactionRequest;
import com.kezbek.transaction.model.response.PotentialCashbackResponse;
import com.kezbek.transaction.model.response.UserTransactionResponse;
import com.kezbek.transaction.repository.PartnerRepository;
import com.kezbek.transaction.repository.TransactionDetailReporsitory;
import com.kezbek.transaction.repository.TransactionRepository;
import com.kezbek.transaction.repository.UserTierRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Transaction Service Test - Exist")
public class TransactionServiceImplTest {
    TransactionServiceImpl transactionService;
    @Mock
    UserTierRepository userTierRepository;
    @Mock
    PartnerRepository partnerRepository;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    KezbekDroolsClientService kezbekDroolsClientService;
    @Mock
    PushNotificationQueueServiceImpl pushNotificationQueueService;
    @Mock
    TopUpEmoney topUpEmoney;
    @Mock
    TransactionDetailReporsitory transactionDetailReporsitory;

    @BeforeEach
    void init() {
        this.transactionService = new TransactionServiceImpl();
        this.transactionService.partnerRepository = partnerRepository;
        this.transactionService.userTierRepository = userTierRepository;
        this.transactionService.transactionRepository = transactionRepository;
        this.transactionService.kezbekDroolsClientService = kezbekDroolsClientService;
        this.transactionService.pushNotificationQueueService = pushNotificationQueueService;
        this.transactionService.topUpEmoney = topUpEmoney;
        this.transactionService.transactionDetailReporsitory = transactionDetailReporsitory;
    }

    @Test
    @DisplayName("Success Add Transaction")
    void testSuccessUserExist() {
        //arrange
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .orderId("123")
                .phone("081234")
                .partnerCode("Tokopedia")
                .amount(new BigDecimal("1500000"))
                .email("syams@gmail.com")
                .totalCheckout(2)
                .build();
        UserTier userTier = UserTier.builder()
                .tierType(TierType.BRONZE)
                .transactionCount(3)
                .email(transactionRequest.getEmail())
                .build();
        Mockito.when(partnerRepository.findByPartnerCode(transactionRequest.getPartnerCode())).thenReturn(Optional.of(Partner.builder().partnerCode("Tokopedia").build()));
        Mockito.when(userTierRepository.findByEmail(transactionRequest.getEmail())).thenReturn(Optional.of(userTier));
        Mockito.when(kezbekDroolsClientService.getCashback(PotentialCashbackRequest.builder()
                .orderId(transactionRequest.getOrderId())
                .totalPrice(transactionRequest.getAmount())
                .totalProduct(transactionRequest.getTotalCheckout())
                .transactionCount(userTier.getTransactionCount() + 1)
                .tierType(userTier.getTierType().name())
                .build())).thenReturn(PotentialCashbackResponse.builder()
                .discountLoyalty(new BigDecimal("18000"))
                .discountTransaction(new BigDecimal("3.5"))
                .tierType(TierType.BRONZE.name())
                .orderId("123")
                .totalPrice(new BigDecimal("15000000"))
                .build());
        when(userTierRepository.save(Mockito.any(UserTier.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(transactionRepository.saveAndFlush(any())).thenReturn(Transaction.builder().id(1L).build()).thenReturn(true);
        Mockito.when(transactionDetailReporsitory.findByTransctionId(any())).thenReturn(TransactionDetail.builder().id(1L).build());
        Mockito.when(topUpEmoney.topup(any(),any())).thenReturn(true);
        //act
        UserTransactionResponse userTransactionResponse = transactionService.getCashback(transactionRequest);
        //assert
        Assertions.assertNotNull(userTransactionResponse);
    }

    @Test
    @DisplayName("Success Add Transaction - User Not Exist")
    void testSuccessUserNotExist() {
        //arrange
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .orderId("123")
                .phone("081234")
                .partnerCode("Tokopedia")
                .amount(new BigDecimal("1500000"))
                .email("syams@gmail.com")
                .totalCheckout(2)
                .build();
        UserTier userTier = null;
        Mockito.when(partnerRepository.findByPartnerCode(transactionRequest.getPartnerCode())).thenReturn(Optional.of(Partner.builder().partnerCode("Tokopedia").build()));
        Mockito.when(userTierRepository.findByEmail(transactionRequest.getEmail())).thenReturn(Optional.empty());
        Mockito.when(kezbekDroolsClientService.getCashback(PotentialCashbackRequest.builder()
                .orderId(transactionRequest.getOrderId())
                .totalPrice(transactionRequest.getAmount())
                .totalProduct(transactionRequest.getTotalCheckout())
                .transactionCount(1)
                .tierType(TierType.BRONZE.name())
                .build())).thenReturn(PotentialCashbackResponse.builder()
                .discountLoyalty(new BigDecimal("18000"))
                .discountTransaction(new BigDecimal("3.5"))
                .tierType(TierType.BRONZE.name())
                .orderId("123")
                .totalPrice(new BigDecimal("15000000"))
                .build());
        when(userTierRepository.save(Mockito.any(UserTier.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(transactionRepository.saveAndFlush(any())).thenReturn(Transaction.builder().id(1L).build()).thenReturn(true);
        Mockito.when(transactionDetailReporsitory.findByTransctionId(any())).thenReturn(TransactionDetail.builder().id(1L).build());
        Mockito.when(topUpEmoney.topup(any(),any())).thenReturn(true);
        //act
        UserTransactionResponse userTransactionResponse = transactionService.getCashback(transactionRequest);
        //assert
        Assertions.assertNotNull(userTransactionResponse);
    }
}
