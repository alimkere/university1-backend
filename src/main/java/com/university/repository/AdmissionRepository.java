package com.university.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.university.models.Admission;

public interface AdmissionRepository extends JpaRepository<Admission, Long>{
	Page<Admission> findByStudentId(Long studentId, Pageable pageable);

	Optional<Admission> findByIdAndStudentId(Long id, Long studentId);


}

