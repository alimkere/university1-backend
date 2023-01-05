package com.university.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import com.university.models.Enrollment;



public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>{
	
	Page<Enrollment> findByStudentId(Long studentId, Pageable pageable);

	Optional<Enrollment> findByIdAndStudentId(Long id, Long studentId);


}
