package com.example.onlinecoursesplatform.dataTrasferObject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class CourseDTO {

    private Long courseId;

    @NotBlank(message = "Course title cannot be blank")
    @Size(max = 255, message = "Course title must be at most 255 characters")
    private String courseTitle;
    @NotBlank(message = "Course content cannot be blank")
    private String courseContent;
    @NotNull(message = "Course category cannot be null")
    private CourseCategoryDTO courseCategory;
    private Date courseDate;
    private String courseAuthor;

    public CourseDTO() {
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseContent() {
        return courseContent;
    }

    public void setCourseContent(String courseContent) {
        this.courseContent = courseContent;
    }

    public CourseCategoryDTO getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(CourseCategoryDTO courseCategory) {
        this.courseCategory = courseCategory;
    }

    public Date getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(Date courseDate) {
        this.courseDate = courseDate;
    }

    public String getCourseAuthor() {
        return courseAuthor;
    }

    public void setCourseAuthor(String courseAuthor) {
        this.courseAuthor = courseAuthor;
    }
}