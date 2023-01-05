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

import com.university.models.Language;
import com.university.models.Student;
import com.university.repository.LanguageRepository;
import com.university.repository.StudentRepository;

@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
@RestController
public class LanguageController {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private LanguageRepository languageRepository;
	
	//Get all languages
    @GetMapping("/languages")
	public List<Language> listLanguages(){
		return languageRepository.findAll();
	}
    
    //Get a language by languageId
    @GetMapping("/languages/{languageId}")
   	public ResponseEntity<Language> getLanguageById(@PathVariable Long languageId){
   		Language language = languageRepository.findById(languageId).
   		orElseThrow(()-> new ResourceNotFoundException("The Language with id "+ languageId +" doesn't exit in the database !!"));
   		
   		return new ResponseEntity<>(language,HttpStatus.OK);
   		
   	}
    
    //Get all languages using studentId
    @GetMapping("/students/{studentId}/languages")
    public Page<Language> getAllLanguagesByStudentId(@PathVariable  Long studentId,
                                                Pageable pageable) {
        return languageRepository.findByStudentId(studentId, pageable);
    }
    
    //Add a language
    @PostMapping("/students/{studentId}/languages")
    public Language createLanguage(@PathVariable  Long studentId,
                                 @Valid @RequestBody Language language) {
        return studentRepository.findById(studentId).map(student -> {
           language.setStudent(student);
            return languageRepository.save(language);
        }).orElseThrow(() -> new ResourceNotFoundException("StudentId " + studentId + " not found"));
    }
    
    //Update a language by using studentId
    @PutMapping("/students/{studentId}/languages/{languageId}")
    public Language updateLanguage(@PathVariable  Long studentId,
                                 @PathVariable  Long languageId,
                                 @Valid @RequestBody Language languageRequest) {
        if(!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("StudentId " + studentId + " not found");
        }

        return languageRepository.findById(languageId).map(language -> {
            language.setName(languageRequest.getName());
            language.setType(languageRequest.getType());
            language.setLevel(languageRequest.getLevel());
            return languageRepository.save(language);
        }).orElseThrow(() -> new ResourceNotFoundException("LanguageId " + languageId + "not found"));
    }
   
  //Patch a Student's language by using languageId
    @PatchMapping("/students/{studentId}/languages/{languageId}")
    public ResponseEntity<Language> patchLanguage(@PathVariable Long studentId,
                                 @PathVariable  Long languageId,
                                 @RequestBody 	Language patch) {
    	Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (!optionalStudent.isPresent()) {
            throw new ResourceNotFoundException("StudentId " + studentId + " not found");
        }
        Optional<Language> optionalLanguage = languageRepository.findById(languageId);
        if (!optionalLanguage.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Language language = optionalLanguage.get();
        language = applyPatch(language, patch);
        language = languageRepository.save(language);
        return ResponseEntity.ok(language);
      }

    private Language applyPatch(Language language, Language patch) {
        if (patch.getType() != null) {
        	language.setType(patch.getType());
        }
        if (patch.getLevel() != null) {
        	language.setLevel(patch.getLevel());
        }
        if (patch.getName() != null) {
        	language.setName(patch.getName());
        }
        
        return language;
    }


    
    //Delete a language using langageId
    @DeleteMapping("/students/{studentId}/languages/{languageId}")
    public ResponseEntity<?> deleteLanguage(@PathVariable (value = "studentId") Long studentId,
                              @PathVariable Long languageId) {
        return languageRepository.findByIdAndStudentId(languageId, studentId).map(language -> {
            languageRepository.delete(language);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Language not found with id " + languageId + " and studentId " + studentId));
    }

}
