package com.kezbek.transaction.repository;

import com.kezbek.transaction.entity.TierType;
import com.kezbek.transaction.entity.UserTier;
//import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserTierRepository extends JpaRepository<UserTier, Long> {
    @Query(
            value = "SELECT * FROM user_tiers ut WHERE email = :email",
            nativeQuery = true
    )
    Optional<UserTier> findByEmail(String email);

    @Query(
            value = "UPDATE user_tiers SET tier_type = :tierType, transaction_count = :transactionCount, last_transaction = :lastTransaction WHERE email = :email",
            nativeQuery = true
    )
    void updateUserTier(String email, TierType tierType, int transactionCount, LocalDateTime lastTransaction);

    @Query(
            value = "UPDATE user_tiers SET tier_type = CASE WHEN tier_type=0 THEN 0 ELSE tier_type-1 END , transaction_count = 0 WHERE user_tiers.last_transaction < now() - '30 days' :: interval",
            nativeQuery = true
    )
    void downGradeTire();
}
