package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

    List<Enquiry> findByMobileNumber(String mobileNumber);

    List<Enquiry> findByStatusOrderByCreatedAtDesc(String status);

    List<Enquiry> findAllByOrderByCreatedAtDesc();
}