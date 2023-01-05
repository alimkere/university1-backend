package com.university.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.university.models.DocStudent;

public interface DocStudentRepository extends JpaRepository<DocStudent, Long>{
	
	Page<DocStudent> findDocByStudentId(Long studentId, Pageable pageable);

	Optional<DocStudent> findByIdAndStudentId(Long id, Long studentId);

}
