package com.ecommerce.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Address Entity
 * Represents user shipping/billing addresses
 */
@Entity
@Table(name = "addresses", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_default", columnList = "user_id, is_default")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "address_type", length = 20)
    @Builder.Default
    private String addressType = "SHIPPING"; // SHIPPING, BILLING, BOTH

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "address_line1", nullable = false, length = 255)
    private String addressLine1;

    @Column(name = "address_line2", length = 255)
    private String addressLine2;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    @Column(name = "is_default")
    @Builder.Default
    private Boolean isDefault = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Get full address as string
     */
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        address.append(addressLine1);
        if (addressLine2 != null && !addressLine2.isEmpty()) {
            address.append(", ").append(addressLine2);
        }
        address.append(", ").append(city);
        address.append(", ").append(state);
        address.append(", ").append(country);
        address.append(" - ").append(postalCode);
        return address.toString();
    }

    /**
     * Override toString to avoid circular reference
     */
    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
