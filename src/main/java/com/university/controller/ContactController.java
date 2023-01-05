package com.university.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.models.Contact;
import com.university.repository.ContactRepository;

@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
@RestController
public class ContactController {
	
	@Autowired
	private ContactRepository contactRepository;
	
	//Get all contacts
	@GetMapping("/contacts")
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
	
	//Get a contact by id
	@GetMapping("/contacts/{contactId}")
    public Contact getContactById(@PathVariable Long contactId) {
        return contactRepository.findById(contactId).orElseThrow(() -> new ResourceNotFoundException("Contact not found with id " + contactId));
    }
	
	//Create a contact
	@PostMapping("/contacts")
    public Contact createContact(@Valid @RequestBody Contact contact) {
        return contactRepository.save(contact);
    }
	
	//Delete a contact
	@DeleteMapping("/contacts/{contactId}")
    public ResponseEntity<?> deleteContact(@PathVariable Long contactId) {
        return contactRepository.findById(contactId).map(contact -> {
        	contactRepository.delete(contact);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("ContactId " + contactId + " not found"));
    }
	

}
