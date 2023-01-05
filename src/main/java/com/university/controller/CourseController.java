package com.university.controller;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import com.university.models.Course;

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

import com.university.repository.CourseRepository;

@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
@RestController
public class CourseController {
	
	@Autowired
	private CourseRepository courseRepository;
	
	@GetMapping("/courses")
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    @GetMapping("/courses/{courseId}")
    public Course getCourseById(@PathVariable Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId));
    }
    
    @PostMapping("/courses")
    public Course createCourse(@Valid @RequestBody Course course) {
        return courseRepository.save(course);
    }
	
    @PutMapping("/courses/{courseId}")
    public Course updateCourse(@PathVariable Long courseId, @Valid @RequestBody Course courseRequest) {
        return courseRepository.findById(courseId).map(course -> {
            course.setName(courseRequest.getName());
            course.setDescription(courseRequest.getDescription());
            course.setLink(courseRequest.getLink());
            course.setImage(courseRequest.getImage());
            return courseRepository.save(course);
        }).orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId));
    }
    
    @PatchMapping("/courses/{courseId}")
    public ResponseEntity<Course> patchCourse(@PathVariable Long courseId, @RequestBody Course patch) {
      // Find the existing course in the database
      Course course = courseRepository.findById(courseId).orElse(null);
      if (course == null) {
        return ResponseEntity.notFound().build();
      }
      // Apply the patch to the course
      course = applyPatch(course, patch);
      // Save the updated course
      course = courseRepository.save(course);
      // Return the updated course
      return ResponseEntity.ok(course);
    }

    private Course applyPatch(Course course, Course patch) {
      if (patch.getName() != null) {
        course.setName(patch.getName());
      }
      if (patch.getDescription() != null) {
        course.setDescription(patch.getDescription());
      }
      if (patch.getLink() != null) {
        course.setLink(patch.getLink());
      }
      if (patch.getImage() != null) {
        course.setImage(patch.getImage());
      }
      return course;
    }
    
    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long courseId) {
        return courseRepository.findById(courseId).map(course -> {
            courseRepository.delete(course);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId));
    }
}

