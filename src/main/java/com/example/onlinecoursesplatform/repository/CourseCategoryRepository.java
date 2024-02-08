package com.example.onlinecoursesplatform.repository;

import com.example.onlinecoursesplatform.model.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory,Long> {

}
