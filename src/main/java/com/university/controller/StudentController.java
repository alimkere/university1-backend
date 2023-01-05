package com.university.controller;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import com.university.models.Student;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.repository.StudentRepository;
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
@RestController
public class StudentController {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    @GetMapping("/students/{studentId}")
    public Student getStudentById(@PathVariable Long studentId) {
        return studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + studentId));
    }
    
    @PostMapping("/students")
    public Student createStudent(@Valid @RequestBody Student student) {
        return studentRepository.save(student);
    }
	
    @PutMapping("/students/{studentId}")
    public Student updateStudent(@PathVariable Long studentId, @Valid @RequestBody Student studentRequest) {
        return studentRepository.findById(studentId).map(student -> {
            student.setFirstName(studentRequest.getFirstName());
            student.setLastName(studentRequest.getLastName());
            student.setCin(studentRequest.getCin());
            student.setPassport(studentRequest.getPassport());
            student.setBirthDay(studentRequest.getBirthDay());
            student.setPicture(studentRequest.getPicture());
            student.setPhone(studentRequest.getPhone());
            student.setEmail(studentRequest.getEmail());
            student.setAdress(studentRequest.getAdress());
            student.setZipCode(studentRequest.getZipCode());
            student.setCountry(studentRequest.getCountry());
            student.setTown(studentRequest.getTown());
            student.setStudyLevel(studentRequest.getStudyLevel());
            student.setStatus(studentRequest.getStatus());
            student.setSex(studentRequest.getSex()); 
            return studentRepository.save(student);
        }).orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + studentId));
    }
    
    @PatchMapping("/students/{studentId}")
    public ResponseEntity<Student> patchStudent(@PathVariable Long studentId, @RequestBody Student patch) {
      // Find the existing student in the database
     Student student = studentRepository.findById(studentId).orElse(null);
      if (student == null) {
        return ResponseEntity.notFound().build();
      }
      // Apply the patch to the student
      student = applyPatch(student, patch);
      // Save the updated student
      student = studentRepository.save(student);
      // Return the updated student
      return ResponseEntity.ok(student);
    }
    private Student applyPatch(Student student, Student patch) {
    	if (patch.getFirstName() != null) {
    	      student.setFirstName(patch.getFirstName());
    	    }
    	if (patch.getLastName() != null) {
  	      student.setLastName(patch.getLastName());
  	    }
    	if (patch.getCin() != null) {
  	      student.setCin(patch.getCin());
  	    }
    	if (patch.getPassport() != null) {
  	      student.setPassport(patch.getPassport());
  	    }
    	if (patch.getBirthDay() != null) {
  	      student.setBirthDay(patch.getBirthDay());
  	    }
    	if (patch.getPicture() != null) {
  	      student.setPicture(patch.getPicture());
  	    }
    	if (patch.getPhone() != null) {
  	      student.setPhone(patch.getPhone());
  	    }
    	if (patch.getEmail() != null) {
    	      student.setEmail(patch.getEmail());
    	    }
    	if (patch.getAdress() != null) {
  	      student.setAdress(patch.getAdress());
  	    }
    	if (patch.getZipCode() != null) {
  	      student.setZipCode(patch.getZipCode());
  	    }
    	if (patch.getCountry() != null) {
  	      student.setCountry(patch.getCountry());
  	    }
    	if (patch.getTown() != null) {
    	      student.setTown(patch.getTown());
    	    }
    	if (patch.getStudyLevel() != null) {
    	      student.setStudyLevel(patch.getStudyLevel());
    	    }
    	if (patch.getStatus() != null) {
    	      student.setStatus(patch.getStatus());
    	    }
    	if (patch.getSex() != null) {
  	      student.setSex(patch.getSex());
  	    }
      return student;
    }
  
 
    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long studentId) {
        return studentRepository.findById(studentId).map(student -> {
        	studentRepository.delete(student);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("StudentId " + studentId + " not found"));
    }
	

}
