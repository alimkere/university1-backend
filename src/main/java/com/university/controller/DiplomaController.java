package com.university.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.models.Diploma;
import com.university.models.Student;
import com.university.repository.DiplomaRepository;
import com.university.repository.StudentRepository;

@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
@RestController
public class DiplomaController {
	
	@Autowired
	private DiplomaRepository diplomaRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	//Get all diplomas
    @GetMapping("/diplomas")
	public List<Diploma> listDiplomas(){
		return diplomaRepository.findAll();
	}
    
    //Get a diploma by diplomaId
    @GetMapping("/diplomas/{diplomaId}")
	public ResponseEntity<Diploma> getDiplomaById(@PathVariable Long diplomaId){
		Diploma diploma = diplomaRepository.findById(diplomaId).
		orElseThrow(()-> new ResourceNotFoundException("The Diploma with id "+ diplomaId +" doesn't exit in the database !!"));
		
		return new ResponseEntity<>(diploma,HttpStatus.OK);
		
	}
    
    //Get all diplomas by studentId
    @GetMapping("/students/{studentId}/diplomas")
    public Page<Diploma> getAllDiplomasByStudentId(@PathVariable  Long studentId,
                                                Pageable pageable) {
        return diplomaRepository.findByStudentId(studentId, pageable);
    }
    
    //Add a diploma using studentId
    @PostMapping("/students/{studentId}/diplomas")
    public Diploma createDiploma(@PathVariable  Long studentId,
                                 @Valid @RequestBody Diploma diploma) {
        return studentRepository.findById(studentId).map(student -> {
           diploma.setStudent(student);
            return diplomaRepository.save(diploma);
        }).orElseThrow(() -> new ResourceNotFoundException("StudentId " + studentId + " not found"));
    }
    
    //Update a diploma
    @PutMapping("/students/{studentId}/diplomas/{diplomaId}")
    public Diploma updateDiploma(@PathVariable  Long studentId,
                                 @PathVariable  Long diplomaId,
                                 @Valid @RequestBody Diploma diplomaRequest) {
        if(!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("StudentId " + studentId + " not found");
        }

        return diplomaRepository.findById(diplomaId).map(diploma -> {
            diploma.setBeginDate(diplomaRequest.getBeginDate());
            diploma.setEndDate(diplomaRequest.getEndDate());
            diploma.setTitle(diplomaRequest.getTitle());
            diploma.setFile(diplomaRequest.getFile());
            diploma.setSerie(diplomaRequest.getSerie());
            diploma.setMention(diplomaRequest.getMention()); 
            diploma.setSchool(diplomaRequest.getSchool()); 
            diploma.setCountry(diplomaRequest.getCountry()); 
            return diplomaRepository.save(diploma);
        }).orElseThrow(() -> new ResourceNotFoundException("DiplomaId " + diplomaId + "not found"));
    }
    
    //Patch a diploma by using studentId
    @PatchMapping("/students/{studentId}/diplomas/{diplomaId}")
    public ResponseEntity<Diploma> patchDiploma(@PathVariable Long studentId,
                                 @PathVariable  Long diplomaId,
                                 @RequestBody Diploma patch) {
    	Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (!optionalStudent.isPresent()) {
            throw new ResourceNotFoundException("StudentId " + studentId + " not found");
        }
        Optional<Diploma> optionalDiploma = diplomaRepository.findById(diplomaId);
        if (!optionalDiploma.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Diploma diploma = optionalDiploma.get();
        diploma = applyPatch(diploma, patch);
        diploma = diplomaRepository.save(diploma);
        return ResponseEntity.ok(diploma);
      }

    private Diploma applyPatch(Diploma diploma, Diploma patch) {
        if (patch.getBeginDate() != null) {
            diploma.setBeginDate(patch.getBeginDate());
        }
        if (patch.getEndDate() != null) {
        	 diploma.setEndDate(patch.getEndDate());
        }
        if (patch.getTitle() != null) {
        	 diploma.setTitle(patch.getTitle());
        }
        if (patch.getSerie() != null) {
        	 diploma.setSerie(patch.getSerie());
        }
        if (patch.getMention() != null) {
        	 diploma.setMention(patch.getMention());
        }
        if (patch.getSchool() != null) {
        	 diploma.setSchool(patch.getSchool());
        }
        if (patch.getFile() != null) {
        	 diploma.setFile(patch.getFile());
        }
        if (patch.getCountry() != null) {
        	 diploma.setCountry(patch.getCountry());
        }
        
        return diploma;
    }

    //Delete a diploma using studentId
    @DeleteMapping("/students/{studentId}/diplomas/{diplomaId}")
    public ResponseEntity<?> deleteDiploma(@PathVariable (value = "studentId") Long studentId,
                              @PathVariable Long diplomaId) {
        return diplomaRepository.findByIdAndStudentId(diplomaId, studentId).map(diploma -> {
            diplomaRepository.delete(diploma);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Diploma not found with id " + diplomaId + " and studentId " + studentId));
    }
}
