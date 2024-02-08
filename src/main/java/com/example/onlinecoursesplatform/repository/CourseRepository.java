package com.example.onlinecoursesplatform.repository;

import com.example.onlinecoursesplatform.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c JOIN c.courseCategory cc WHERE LOWER(c.courseTitle) LIKE LOWER(CONCAT('%', :titleKeyword, '%')) AND LOWER(cc.categoryName) LIKE LOWER(CONCAT(:categoryKeyword, '%'))")
    List<Course> findCoursesByTitleOrCategoryName(@Param("titleKeyword") String titleKeyword, @Param("categoryKeyword") String categoryKeyword);
    Optional<Course> findByCourseTitle(String courseTitle);
}
