package com.university.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.university.models.DocEmploye;

public interface DocEmployeRepository extends JpaRepository<DocEmploye, Long>{
	
	Page<DocEmploye> findByEmployeId(Long employeId, Pageable pageable);
	
	Optional<DocEmploye> findByIdAndEmployeId(Long id, Long employeId);

}
