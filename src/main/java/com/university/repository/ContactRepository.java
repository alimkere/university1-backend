package com.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.university.models.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>{

}
