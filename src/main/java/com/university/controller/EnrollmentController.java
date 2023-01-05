package com.university.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.models.Enrollment;
import com.university.repository.EnrollmentRepository;
import com.university.repository.StudentRepository;

@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
@RestController
public class EnrollmentController {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private EnrollmentRepository enrollmentRepository;
	
	//Get all enrollments
	@GetMapping("/enrollments")
	public List<Enrollment> listEnrollments(){
		return enrollmentRepository.findAll();
	}
    
	//Get an enrollment by enrollmentId
    @GetMapping("/enrollments/{enrollmentId}")
   	public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long enrollmentId){
    	Enrollment enrollment = enrollmentRepository.findById(enrollmentId).
   		orElseThrow(()-> new ResourceNotFoundException("The Enrollment with id "+ enrollmentId +" doesn't exit in the database !!"));
   		
   		return new ResponseEntity<>(enrollment,HttpStatus.OK);
   		
   	}
    
    //Get all enrollments using studentId
    @GetMapping("/students/{studentId}/enrollments")
    public Page<Enrollment> getAllEnrollmentsByStudentId(@PathVariable  Long studentId,
                                                Pageable pageable) {
        return enrollmentRepository.findByStudentId(studentId, pageable);
    }
    
    //Add an enrollment using studentId
    @PostMapping("/students/{studentId}/enrollments")
    public Enrollment createEnrollment(@PathVariable  Long studentId,
                                 @Valid @RequestBody Enrollment enrollment) {
        return studentRepository.findById(studentId).map(student -> {
        	enrollment.setStudent(student);
            return enrollmentRepository.save(enrollment);
        }).orElseThrow(() -> new ResourceNotFoundException("StudentId " + studentId + " not found"));
    }
    
    //Update an enrollment using studentId
    @PutMapping("/students/{studentId}/enrollments/{enrollmentId}")
    public Enrollment updateEnrollment(@PathVariable  Long studentId,
                                 @PathVariable  Long enrollmentId,
                                 @Valid @RequestBody Enrollment enrollmentRequest) {
        if(!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("StudentId " + studentId + " not found");
        }

        return enrollmentRepository.findById(enrollmentId).map(enrollment -> {
        	enrollment.setSession(enrollmentRequest.getSession());
            return enrollmentRepository.save(enrollment);
        }).orElseThrow(() -> new ResourceNotFoundException("EnrollmentId " + enrollmentId + "not found"));
    }
    
    //Delete an enrollment using studentId
    @DeleteMapping("/students/{studentId}/enrollments/{enrollmentId}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable (value = "studentId") Long studentId,
                              @PathVariable Long enrollmentId) {
        return enrollmentRepository.findByIdAndStudentId(enrollmentId, studentId).map(enrollment -> {
        	enrollmentRepository.delete(enrollment);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id " + enrollmentId + " and studentId " + studentId));
    }
	

}
