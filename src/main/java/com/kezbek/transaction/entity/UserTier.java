package com.kezbek.transaction.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_tiers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTier extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotEmpty
    private String email;
    private TierType tierType = TierType.BRONZE;
    private int transactionCount;
    private LocalDateTime lastTransaction;
}
