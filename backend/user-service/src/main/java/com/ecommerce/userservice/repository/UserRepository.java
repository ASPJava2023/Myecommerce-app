package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository
 * Data access layer for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by OAuth provider and provider ID
     */
    Optional<User> findByOauthProviderAndOauthProviderId(String oauthProvider, String oauthProviderId);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find users by status
     */
    Page<User> findByStatus(String status, Pageable pageable);

    /**
     * Find users by role name
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    Page<User> findByRoleName(@Param("roleName") String roleName, Pageable pageable);

    /**
     * Search users by name or email
     */
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find active users
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    Page<User> findActiveUsers(Pageable pageable);

    /**
     * Count users by status
     */
    long countByStatus(String status);

    /**
     * Count users by role
     */
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName")
    long countByRoleName(@Param("roleName") String roleName);
}
