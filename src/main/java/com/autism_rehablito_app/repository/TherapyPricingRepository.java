package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.TherapyPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TherapyPricingRepository extends JpaRepository<TherapyPricing, Long> {

    /*
     * Find pricing by therapy type.
     */
    Optional<TherapyPricing> findByTherapyType(String therapyType);

    /*
     * Find only active therapy pricing options.
     */
    List<TherapyPricing> findByActiveTrueOrderByTherapyTypeAsc();

    /*
     * Check whether a therapy type already exists.
     */
    boolean existsByTherapyType(String therapyType);
}