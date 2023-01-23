package com.kezbek.transaction.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TransactionRequest {
    private String orderId;
    private String email;
    private String phone;
    private int totalCheckout;
    private BigDecimal amount;
    private String partnerCode;
}