package com.example.onlinecoursesplatform.service;

import com.example.onlinecoursesplatform.dataTrasferObject.CourseDTO;
import com.example.onlinecoursesplatform.exceptions.course.CourseInvalidException;
import com.example.onlinecoursesplatform.exceptions.course.CourseNotFoundException;
import com.example.onlinecoursesplatform.exceptions.course.CourseServiceException;
import com.example.onlinecoursesplatform.exceptions.ipAddress.IpAddressFetchException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CourseService {

    CourseDTO getCourseById(Long courseId) throws CourseNotFoundException;

    CourseDTO updateCourse(Long courseId, CourseDTO updatedCourseDTO, UserDetails userDetails) throws CourseNotFoundException, CourseInvalidException, IpAddressFetchException, CourseServiceException;

    void deleteCourse(Long courseId) throws CourseNotFoundException, CourseServiceException;

    List<CourseDTO> findCoursesByTitleOrCategoryName(String titleKeyword, String categoryKeyword);

    List<CourseDTO> getAllCourses();

    CourseDTO addCourse(CourseDTO courseDTO, UserDetails userDetails) throws CourseInvalidException, IpAddressFetchException, CourseServiceException;

}
