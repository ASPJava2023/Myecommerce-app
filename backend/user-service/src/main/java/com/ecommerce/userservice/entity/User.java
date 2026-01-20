package com.ecommerce.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * User Entity
 * Represents a user account in the system
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_oauth", columnList = "oauth_provider, oauth_provider_id"),
        @Index(name = "idx_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "is_email_verified")
    @Builder.Default
    private Boolean isEmailVerified = false;

    @Column(name = "is_phone_verified")
    @Builder.Default
    private Boolean isPhoneVerified = false;

    @Column(name = "oauth_provider", length = 50)
    private String oauthProvider; // GOOGLE, FACEBOOK, null for local

    @Column(name = "oauth_provider_id", length = 255)
    private String oauthProviderId;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Column(length = 20)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, SUSPENDED

    @Column(name = "preferred_currency_id")
    private Long preferredCurrencyId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Address> addresses = new HashSet<>();

    @Version
    private Integer version;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    /**
     * Helper method to add a role
     */
    public void addRole(Role role) {
        this.roles.add(role);
    }

    /**
     * Helper method to remove a role
     */
    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    /**
     * Helper method to add an address
     */
    public void addAddress(Address address) {
        this.addresses.add(address);
        address.setUser(this);
    }

    /**
     * Helper method to remove an address
     */
    public void removeAddress(Address address) {
        this.addresses.remove(address);
        address.setUser(null);
    }

    /**
     * Check if user has a specific role
     */
    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    /**
     * Get full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Check if user is active
     */
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    /**
     * Check if user is OAuth user
     */
    public boolean isOAuthUser() {
        return oauthProvider != null && !oauthProvider.isEmpty();
    }
}
