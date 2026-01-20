package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Password Reset Token Repository
 * Data access layer for PasswordResetToken entity
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Find token by token string
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Find valid token for user
     */
    @Query("SELECT t FROM PasswordResetToken t WHERE t.user.id = :userId " +
            "AND t.isUsed = false AND t.expiryDate > :now ORDER BY t.createdAt DESC")
    Optional<PasswordResetToken> findValidTokenByUserId(@Param("userId") Long userId,
            @Param("now") LocalDateTime now);

    /**
     * Delete expired tokens
     */
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    /**
     * Delete all tokens for a user
     */
    void deleteByUserId(Long userId);
}
