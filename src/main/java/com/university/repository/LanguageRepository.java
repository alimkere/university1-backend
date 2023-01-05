package com.university.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.university.models.Language;

public interface LanguageRepository extends JpaRepository<Language, Long>{
	Page<Language> findByStudentId(Long studentId, Pageable pageable);

	Optional<Language> findByIdAndStudentId(Long id, Long studentId);

}
