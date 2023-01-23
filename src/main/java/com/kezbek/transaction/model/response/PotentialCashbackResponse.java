package com.kezbek.transaction.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PotentialCashbackResponse {
    private String orderId;
    private String tierType;
    private BigDecimal totalPrice;
    private Integer totalProduct;
    private Integer transactionCount;
    private BigDecimal discountTransaction;
    private BigDecimal discountLoyalty;
}
