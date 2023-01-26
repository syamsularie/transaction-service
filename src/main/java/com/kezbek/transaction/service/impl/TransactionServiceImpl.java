package com.kezbek.transaction.service.impl;

import com.kezbek.transaction.entity.*;
import com.kezbek.transaction.external.TopUpEmoney;
import com.kezbek.transaction.feign.KezbekDroolsClientService;
import com.kezbek.transaction.model.dto.CashbackDTO;
import com.kezbek.transaction.model.request.PotentialCashbackRequest;
import com.kezbek.transaction.model.request.TransactionRequest;
import com.kezbek.transaction.model.response.PotentialCashbackResponse;
import com.kezbek.transaction.model.response.UserTransactionResponse;
import com.kezbek.transaction.repository.PartnerRepository;
import com.kezbek.transaction.repository.TransactionDetailReporsitory;
import com.kezbek.transaction.repository.TransactionRepository;
import com.kezbek.transaction.repository.UserTierRepository;
import com.kezbek.transaction.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionDetailReporsitory transactionDetailReporsitory;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserTierRepository userTierRepository;
    @Autowired
    KezbekDroolsClientService kezbekDroolsClientService;
    @Autowired
    PartnerRepository partnerRepository;
    @Autowired
    PushNotificationQueueServiceImpl pushNotificationQueueService;
    @Autowired
    TopUpEmoney topUpEmoney;

    static TierType nextTier(TierType currentTier) {
        switch (currentTier) {
            case BRONZE:
                return TierType.SILVER;
            case SILVER:
                return TierType.GOLD;
            case GOLD:
                return currentTier;
            default:
                return TierType.BRONZE;
        }
    }

    @Override
    public UserTransactionResponse getCashback(TransactionRequest transactionRequest) {

        if (partnerRepository.findByPartnerCode(transactionRequest.getPartnerCode()).isEmpty()) {
            log.error("Partner Not Found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found");
        }
        PotentialCashbackResponse cashbackResponse = userTierRepository.findByEmail(transactionRequest.getEmail()).map(userTier ->
                kezbekDroolsClientService.getCashback(PotentialCashbackRequest.builder()
                        .orderId(transactionRequest.getOrderId())
                        .totalPrice(transactionRequest.getAmount())
                        .totalProduct(transactionRequest.getTotalCheckout())
                        .transactionCount(userTier.getTransactionCount() + 1)
                        .tierType(userTier.getTierType().name())
                        .build())
        ).orElse(
                kezbekDroolsClientService.getCashback(PotentialCashbackRequest.builder()
                        .orderId(transactionRequest.getOrderId())
                        .totalPrice(transactionRequest.getAmount())
                        .totalProduct(transactionRequest.getTotalCheckout())
                        .transactionCount(1)
                        .tierType(TierType.BRONZE.name())
                        .build())
        );
        userTierRepository.save(userTierRepository.findByEmail(transactionRequest.getEmail()).map(user -> {
            user.setTransactionCount(user.getTransactionCount() == 7 ? 1 : user.getTransactionCount() + 1);
            user.setLastTransaction(LocalDateTime.now());
            user.setTierType((user.getTransactionCount() == 7 ? nextTier(user.getTierType()) : user.getTierType()));
            return user;
        }).orElse(UserTier.builder()
                .email(transactionRequest.getEmail())
                .transactionCount(1)
                .lastTransaction(LocalDateTime.now())
                .tierType(TierType.BRONZE)
                .build()
        ));
        CashbackDTO cashbackDTO = this.cashbackTransaction(transactionRequest, cashbackResponse);

        UserTransactionResponse response = UserTransactionResponse.builder()
                .email(transactionRequest.getEmail())
                .phone(transactionRequest.getPhone())
                .cashbackTransaction(cashbackDTO.getCashbackTransaction())
                .cashbackLoyalty(cashbackDTO.getCashbackLoyalty())
                .totalCashback(cashbackDTO.getTotalCashback())
                .build();
        pushNotificationQueueService.execute(response);
        return response;
    }

    private CashbackDTO cashbackTransaction(TransactionRequest transactionRequest, PotentialCashbackResponse cashbackResponse) {
        BigDecimal cashbackTransaction = cashbackResponse.getDiscountTransaction() == null ? BigDecimal.ZERO :
                cashbackResponse.getDiscountTransaction();
        BigDecimal cashbackLoyalty = cashbackResponse.getDiscountLoyalty() == null ? BigDecimal.ZERO :
                cashbackResponse.getDiscountLoyalty();
        BigDecimal totalCashback = (cashbackTransaction.divide(new BigDecimal(100)).multiply(transactionRequest.getAmount())).add(cashbackLoyalty);
        Transaction transaction = Transaction.builder()
                .email(transactionRequest.getEmail())
                .phone(transactionRequest.getPhone())
                .build();
        transaction.setTransactionDetail(TransactionDetail.builder()
                .totalTransaction(transactionRequest.getTotalCheckout())
                .transaction(transaction)
                .totalAmount(transactionRequest.getAmount())
                .totalTransaction(transactionRequest.getTotalCheckout())
                .status(StatusTransaction.PROGRESS)
                .cashbackTransaction(cashbackTransaction)
                .cashbackLoyalty(cashbackLoyalty)
                .totalCashback(totalCashback)
                .build()
        );

        Transaction trx = transactionRepository.saveAndFlush(transaction);
        if (topUpEmoney.topup(transactionRequest.getPhone(),totalCashback)){
            TransactionDetail  transactionDetail = transactionDetailReporsitory.findByTransctionId(trx.getId());
            transactionDetail.setStatus(StatusTransaction.DONE);
            transactionDetailReporsitory.save(transactionDetail);
        }
        return CashbackDTO.builder()
                .cashbackTransaction(cashbackTransaction)
                .cashbackLoyalty(cashbackLoyalty)
                .totalCashback(totalCashback)
                .build();
    }
}
