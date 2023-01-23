package com.kezbek.transaction.repository;

import com.kezbek.transaction.entity.TransactionDetail;
import com.kezbek.transaction.entity.UserTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionDetailReporsitory extends JpaRepository<TransactionDetail, Long> {
    @Query(
            value = "SELECT * FROM transaction_details td WHERE transaction_id = :id",
            nativeQuery = true
    )
    TransactionDetail findByTransctionId(Long id);
}
