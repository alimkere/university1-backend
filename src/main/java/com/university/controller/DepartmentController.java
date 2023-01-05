package com.university.controller;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import com.university.models.Department;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.university.repository.DepartmentRepository;

@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
@RestController
public class DepartmentController {
	
	@Autowired
	private DepartmentRepository departmentRepository;

	//Get all departments
	@GetMapping("/departments")
    public ResponseEntity < List<Department> > getAllDepartments(){
		
		List<Department> listDepartments = departmentRepository.findAll();
		
		if(listDepartments.size() == 0) {
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		}
		
		return new ResponseEntity<>(listDepartments,HttpStatus.OK);
	}
	
	//Get a department by id
	@GetMapping("/departments/{departmentId}")
	public ResponseEntity<Department> getDepartmentById(@PathVariable Long departmentId){
		Department department = departmentRepository.findById(departmentId).
		orElseThrow(()-> new ResourceNotFoundException("The Department with id "+ departmentId +"doesn't exit in the database !!"));
		
		return new ResponseEntity<>(department,HttpStatus.OK);
		
	}
	
	//Create a department
    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department) {
		try {
			Department _department = departmentRepository.save(department);
		  return new ResponseEntity<>(_department, HttpStatus.CREATED);
		} catch (Exception e) {
		  return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		}
    
    //Update a department
    @PutMapping("/departments/{departmentId}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long departmentId, @Valid  @RequestBody Department department) {
	    Optional<Department> departmentData = departmentRepository.findById(departmentId);
	    if (departmentData.isPresent()) {
	    	Department _department = departmentData.get();
	    	_department.setName(department.getName());
	      return new ResponseEntity<>(departmentRepository.save(_department), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	 }

    //Delete a department
    @DeleteMapping("/departments/{departmentId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long departmentId) {
        return departmentRepository.findById(departmentId).map(department -> {
        	departmentRepository.delete(department);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("DepartmentId " + departmentId + " not found"));
    }

	
}
