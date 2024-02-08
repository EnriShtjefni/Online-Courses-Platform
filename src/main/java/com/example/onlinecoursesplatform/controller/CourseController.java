package com.example.onlinecoursesplatform.controller;

import com.example.onlinecoursesplatform.dataTrasferObject.CourseDTO;
import com.example.onlinecoursesplatform.exceptions.course.CourseInvalidException;
import com.example.onlinecoursesplatform.exceptions.course.CourseServiceException;
import com.example.onlinecoursesplatform.exceptions.ipAddress.IpAddressFetchException;
import com.example.onlinecoursesplatform.service.CourseServiceImpl;
import com.example.onlinecoursesplatform.exceptions.course.CourseNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseServiceImpl courseService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            CourseDTO courseDTO = courseService.getCourseById(id);
            return new ResponseEntity<>(courseDTO, HttpStatus.OK);
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody CourseDTO updatedCourseDTO,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        try {
            CourseDTO updatedCourse = courseService.updateCourse(id, updatedCourseDTO, userDetails);
            return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (CourseInvalidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IpAddressFetchException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (CourseServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable() Long id) {
        try {
            courseService.deleteCourse(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found: " + e.getMessage());
        } catch (CourseServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Course Service Error: " + e.getMessage());
        }
    }

    @GetMapping("/filter")
    public List<CourseDTO> filterCourses(@RequestParam() String titleKeyword, @RequestParam() String categoryKeyword) {
        return courseService.findCoursesByTitleOrCategoryName(titleKeyword, categoryKeyword);
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = courseService.getAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<?> addCourse(@RequestBody CourseDTO newCourseDTO,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            CourseDTO addedCourse = courseService.addCourse(newCourseDTO, userDetails);
            return new ResponseEntity<>(addedCourse, HttpStatus.CREATED);
        } catch (CourseInvalidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IpAddressFetchException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (CourseServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

}