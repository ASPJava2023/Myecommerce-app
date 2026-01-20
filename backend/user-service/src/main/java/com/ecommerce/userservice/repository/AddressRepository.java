package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Address Repository
 * Data access layer for Address entity
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Find all addresses for a user
     */
    List<Address> findByUserId(Long userId);

    /**
     * Find default address for a user
     */
    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);

    /**
     * Find addresses by type for a user
     */
    List<Address> findByUserIdAndAddressType(Long userId, String addressType);

    /**
     * Unset all default addresses for a user
     */
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId")
    void unsetDefaultAddresses(@Param("userId") Long userId);

    /**
     * Count addresses for a user
     */
    long countByUserId(Long userId);

    /**
     * Delete all addresses for a user
     */
    void deleteByUserId(Long userId);
}
