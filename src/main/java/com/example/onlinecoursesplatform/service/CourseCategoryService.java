package com.example.onlinecoursesplatform.service;

import com.example.onlinecoursesplatform.exceptions.course.CourseNotFoundException;
import com.example.onlinecoursesplatform.model.CourseCategory;
import com.example.onlinecoursesplatform.repository.CourseCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseCategoryService {
    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    public CourseCategory findCategoryById(Long categoryId) throws CourseNotFoundException {
        return courseCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new CourseNotFoundException("Course category with ID '" + categoryId + "' not found."));
    }
}
