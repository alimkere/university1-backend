package com.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.university.models.Student;

//collectionRessourceRel = Name of JSON entry
//path = reference for student path /student
//path is in singular form because of mining of mapping one to many
//@RepositoryRestResource
public interface StudentRepository extends JpaRepository<Student, Long>{

}
