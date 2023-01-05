package com.university.controller;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.university.models.Department;
import com.university.models.Employe;
import com.university.repository.DepartmentRepository;
import com.university.repository.EmployeRepository;

@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
@RestController
public class EmployeController {
	@Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    
    //Get all employees
    @GetMapping("/employes")
	public List<Employe> listEmployes(){
		return employeRepository.findAll();
	}
    
    //Get an employee with employeId
    @GetMapping("/employes/{employeId}")
	public ResponseEntity<Employe> getEmployeById(@PathVariable Long employeId){
		Employe employe = employeRepository.findById(employeId).
		orElseThrow(()-> new ResourceNotFoundException("The Employe with id "+ employeId +" doesn't exit in the database !!"));
		
		return new ResponseEntity<>(employe,HttpStatus.OK);
		
	}

  //Get all employees by departmentId
    @GetMapping("/departments/{departmentId}/employes")
    public Page<Employe> getAllEmployesByDepartmentId(@PathVariable (value = "departmentId") Long departmentId,
                                                Pageable pageable) {
        return employeRepository.findByDepartmentId(departmentId, pageable);
    }

    //Create an employee by departmentId
    @PostMapping("/departments/{departmentId}/employes")
    public Employe createEmploye(@PathVariable (value = "departmentId") Long departmentId,
                                 @Valid @RequestBody Employe employe) {
        return departmentRepository.findById(departmentId).map(department -> {
           employe.setDepartment(department);
            return employeRepository.save(employe);
        }).orElseThrow(() -> new ResourceNotFoundException("DepartmentId " + departmentId + " not found"));
    }
    
    //Update an employe by departmentId
    @PutMapping("/departments/{departmentId}/employes/{employeId}")
    public Employe updateEmploye(@PathVariable (value = "departmentId") Long departmentId,
                                 @PathVariable (value = "employeId") Long employeId,
                                 @Valid @RequestBody Employe employeRequest) {
        if(!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("DepartmentId " + departmentId + " not found");
        }

        return employeRepository.findById(employeId).map(employe -> {
            employe.setFirstName(employeRequest.getFirstName());
            employe.setLastName(employeRequest.getLastName());
            employe.setEmail(employeRequest.getEmail());
            employe.setPhone(employeRequest.getPhone());
            employe.setPoste(employeRequest.getPoste());
            employe.setPicture(employeRequest.getPicture());
            return employeRepository.save(employe);
        }).orElseThrow(() -> new ResourceNotFoundException("EmployeId " + employeId + "not found"));
    }
    
    //Update a property of an employe
    @PatchMapping("/departments/{departmentId}/employes/{employeId}")
    public ResponseEntity<Employe> patchEmploye(@PathVariable (value = "departmentId") Long departmentId,
                                 @PathVariable (value = "employeId") Long employeId,
                                 @RequestBody Employe patch) {
    	Optional<Department> optionalDepartment = departmentRepository.findById(departmentId);
        if (!optionalDepartment.isPresent()) {
            throw new ResourceNotFoundException("DepartmentId " + departmentId + " not found");
        }
        Optional<Employe> optionalEmploye = employeRepository.findById(employeId);
        if (!optionalEmploye.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Employe employe = optionalEmploye.get();
        employe = applyPatch(employe, patch);
        employe = employeRepository.save(employe);
        return ResponseEntity.ok(employe);
      }

    private Employe applyPatch(Employe employe, Employe patch) {
        if (patch.getFirstName() != null) {
            employe.setFirstName(patch.getFirstName());
        }
        if (patch.getLastName() != null) {
            employe.setLastName(patch.getLastName());
        }
        if (patch.getEmail() != null) {
            employe.setEmail(patch.getEmail());
        }
        if (patch.getPhone() != null) {
            employe.setPhone(patch.getPhone());
        }
        if (patch.getPoste() != null) {
            employe.setPoste(patch.getPoste());
        }
        if (patch.getPicture() != null) {
            employe.setPicture(patch.getPicture());
        }
        return employe;
    }
   
    //Delete an employe by departmentId
    @DeleteMapping("/departments/{departmentId}/employes/{employeId}")
    public ResponseEntity<?> deleteEmploye(@PathVariable (value = "departmentId") Long departmentId,
                              @PathVariable (value = "employeId") Long employeId) {
        return employeRepository.findByIdAndDepartmentId(employeId, departmentId).map(employe -> {
            employeRepository.delete(employe);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Employe not found with id " + employeId + " and departmentId " + departmentId));
    }

}
