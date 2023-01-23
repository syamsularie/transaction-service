package com.kezbek.transaction.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Table(name = "partners")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String partnerCode;
    @NotNull
    @NotEmpty
    private String name;
    private String description;
    private String category;
    @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}
