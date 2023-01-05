package com.university.models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

	@NotBlank
    @Column(name = "first_name")
    private String firstName;
    
	@NotBlank
    @Column(name = "last_name")
    private String lastName ;
    
	@NotBlank
    @Column(name = "cin", unique = true)
    private String cin;
    
	@NotBlank
    @Column(name = "passport", unique = true)
    private String passport;
    
	@NotNull
    @Column(name = "birth_day")
    private Date birthDay;
    
	@NotBlank
    @Column(name = "picture")
    private String picture;
    
	@NotBlank
    @Column(name = "phone",unique = true)
    private String phone;
	
	@NotBlank
    @Column(name = "email",unique = true)
    private String email;
    
	@NotBlank
    @Column(name = "adress")
    private String adress;
    
	@NotBlank
    @Column(name = "zip_code", unique = true)
    private String zipCode;
    
	@NotBlank
    @Column(name = "country")
    private String country;
    
	@NotBlank
    @Column(name = "town")
    private String town;

	@NotBlank
    @Column(name = "study_level")
    private String studyLevel;
    
	@NotBlank
    @Column(name = "status")
    private String status;
    
	@NotBlank
    @Column(name = "sex")
    private String sex;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diploma> diplomas;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocStudent> docStudents;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Language> languages;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Admission> admissions;
    
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Enrollment enrollment;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
	@CreationTimestamp
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Date updatedAt;

}
