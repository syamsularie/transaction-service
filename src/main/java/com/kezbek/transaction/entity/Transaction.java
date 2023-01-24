package com.kezbek.transaction.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotEmpty
    @Size(min = 10, max = 15)
    private String phone;
    @NotNull
    @NotEmpty
    private String email;
    @ManyToOne
    @JoinColumn(name = "partner_code")
    private Partner partner;
    @OneToOne(mappedBy = "transaction", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private TransactionDetail transactionDetail;
}
