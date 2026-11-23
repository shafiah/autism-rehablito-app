package com.autism_rehablito_app.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "therapy_pricing",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_therapy_pricing_type",
                        columnNames = "therapy_type"
                )
        }
)
public class TherapyPricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Therapy type available for payment.
     *
     * Examples:
     * Occupational Therapy
     * Speech Therapy
     * Both Occupational + Speech Therapy
     */
    @Column(name = "therapy_type", nullable = false, length = 100)
    private String therapyType;

    /*
     * Current fee for this therapy type.
     */
    @Column(name = "fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal fee;

    /*
     * Admin can use this field later to enable/disable
     * a therapy pricing option.
     */
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (active == null) {
            active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        if (active == null) {
            active = true;
        }
    }

    public Long getId() {
        return id;
    }

    public String getTherapyType() {
        return therapyType;
    }

    public void setTherapyType(String therapyType) {
        this.therapyType = therapyType;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}