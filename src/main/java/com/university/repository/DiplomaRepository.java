package com.university.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.university.models.Diploma;

public interface DiplomaRepository extends JpaRepository<Diploma, Long>{
	
	Page<Diploma> findByStudentId(Long studentId, Pageable pageable);

	Optional<Diploma> findByIdAndStudentId(Long id, Long studentId);

}
