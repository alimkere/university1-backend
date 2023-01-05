package com.university.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.university.models.Payment;


public interface PaymentRepository extends JpaRepository<Payment, Long>{
	
	Page<Payment> findByEnrollmentId(Long studentId, Pageable pageable);

	Optional<Payment> findByIdAndEnrollmentId(Long id, Long studentId);

	
}
