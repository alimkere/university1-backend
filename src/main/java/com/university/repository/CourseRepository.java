package com.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.university.models.Course;

public interface CourseRepository extends JpaRepository<Course, Long>{

}
