package com.kezbek.transaction.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PotentialCashbackRequest {
    private String orderId;
    private String tierType;
    private BigDecimal totalPrice;
    private Integer totalProduct;
    private Integer transactionCount;
    private BigDecimal discountTransaction;
    private BigDecimal discountLoyalty;
}
