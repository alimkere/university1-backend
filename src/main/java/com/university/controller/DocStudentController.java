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

import com.university.models.DocStudent;
import com.university.models.Student;
import com.university.repository.DocStudentRepository;
import com.university.repository.StudentRepository;

@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
@RestController
public class DocStudentController {
	
	@Autowired
	private DocStudentRepository docStudentRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	//Get all documents
    @GetMapping("/docstudents")
	public List<DocStudent> listDocuments(){
		return docStudentRepository.findAll();
	}
    
    //Get a Student's document by docStudentId
    @GetMapping("/docstudents/{documentId}")
	public ResponseEntity<DocStudent> getDocStudentById(@PathVariable Long documentId){
    	DocStudent docStudent = docStudentRepository.findById(documentId).
		orElseThrow(()-> new ResourceNotFoundException("The Document with id "+ documentId +" doesn't exit in the database !!"));
		
		return new ResponseEntity<>(docStudent,HttpStatus.OK);
		
	}
    
    //Get all students'documents by studentId
    @GetMapping("/students/{studentId}/docstudents")
    public Page<DocStudent> getAllDocumentsByStudentId(@PathVariable  Long studentId,
                                                Pageable pageable) {
        return docStudentRepository.findDocByStudentId(studentId, pageable);
    }
    
    @PostMapping("/students/{studentId}/docstudents")
    public DocStudent createDocumentByStudentId(@PathVariable  Long studentId,
                                 @Valid @RequestBody DocStudent docStudent) {
        return studentRepository.findById(studentId).map(student -> {
           docStudent.setStudent(student);
            return docStudentRepository.save(docStudent);
        }).orElseThrow(() -> new ResourceNotFoundException("StudentId " + studentId + " not found"));
    }
    
    //Update a student's document
    @PutMapping("/students/{studentId}/docstudents/{documentId}")
    public DocStudent updateDocumentFromStudent(@PathVariable  Long studentId,
                                 @PathVariable  Long documentId,
                                 @Valid @RequestBody DocStudent documentRequest) {
        if(!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("StudentId " + studentId + " not found");
        }

        return docStudentRepository.findById(documentId).map(document -> {
            document.setName(documentRequest.getName());
            document.setFile(documentRequest.getFile());
            return docStudentRepository.save(document);
        }).orElseThrow(() -> new ResourceNotFoundException("DocumentId " + documentId + "not found"));
    }
    
  //Patch a Student's document by using studentId
    @PatchMapping("/students/{studentId}/docstudents/{documentId}")
    public ResponseEntity<DocStudent> patchDocStudent(@PathVariable Long studentId,
                                 @PathVariable  Long docStudentId,
                                 @RequestBody DocStudent patch) {
    	Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (!optionalStudent.isPresent()) {
            throw new ResourceNotFoundException("StudentId " + studentId + " not found");
        }
        Optional<DocStudent> optionalDocStudent = docStudentRepository.findById(docStudentId);
        if (!optionalDocStudent.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        DocStudent docStudent = optionalDocStudent.get();
        docStudent = applyPatch(docStudent, patch);
        docStudent = docStudentRepository.save(docStudent);
        return ResponseEntity.ok(docStudent);
      }

    private DocStudent applyPatch(DocStudent docStudent, DocStudent patch) {
        if (patch.getFile() != null) {
        	docStudent.setFile(patch.getFile());
        }
        if (patch.getName() != null) {
        	docStudent.setName(patch.getName());
        }
        
        return docStudent;
    }


    //Delete a student document by studentId
    @DeleteMapping("/students/{studentId}/docstudents/{documentId}")
    public ResponseEntity<?> deleteDocumentFromStudent(@PathVariable Long studentId,
                              @PathVariable Long documentId) {
        return docStudentRepository.findByIdAndStudentId(documentId, studentId).map(document -> {
            docStudentRepository.delete(document);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Document not found with id " + documentId + " and studentId " + studentId));
    }

}
