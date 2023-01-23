package com.kezbek.transaction.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "transaction_details")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
    private BigDecimal totalAmount;
    private int totalTransaction;
    private BigDecimal cashbackTransaction;
    private BigDecimal cashbackLoyalty;
    private BigDecimal totalCashback;
    private StatusTransaction status = StatusTransaction.PROGRESS;
}
