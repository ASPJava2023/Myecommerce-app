package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Refresh Token Repository
 * Data access layer for RefreshToken entity
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Find token by token string
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Find valid token for user
     */
    @Query("SELECT t FROM RefreshToken t WHERE t.user.id = :userId " +
            "AND t.isRevoked = false AND t.expiryDate > :now ORDER BY t.createdAt DESC")
    Optional<RefreshToken> findValidTokenByUserId(@Param("userId") Long userId,
            @Param("now") LocalDateTime now);

    /**
     * Revoke all tokens for a user
     */
    @Modifying
    @Query("UPDATE RefreshToken t SET t.isRevoked = true WHERE t.user.id = :userId")
    void revokeAllUserTokens(@Param("userId") Long userId);

    /**
     * Delete expired tokens
     */
    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    /**
     * Delete all tokens for a user
     */
    void deleteByUserId(Long userId);
}
