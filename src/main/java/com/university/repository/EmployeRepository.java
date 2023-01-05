package com.university.repository;


import org.springframework.data.domain.Pageable;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.university.models.Employe;


public interface EmployeRepository extends JpaRepository<Employe, Long>{
	
	Page<Employe> findByDepartmentId(Long departmentId, Pageable pageable);

	Optional<Employe> findByIdAndDepartmentId(Long id, Long departmentId);

}
