package com.example.onlinecoursesplatform.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "Course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_title", nullable = false, unique = true)
    private String courseTitle;

    @Column(name = "course_content", nullable = false)
    private String courseContent;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CourseCategory courseCategory;

    @Temporal(TemporalType.DATE)
    @Column(name = "last_modified", columnDefinition = "DATE DEFAULT CURRENT_DATE", nullable = false)
    @UpdateTimestamp()
    private Date courseDate;

    @Column(name = "course_author")
    private String courseAuthor;

    @Column(name = "author_ip_address")
    private String authorIpAddress;

    public Course() {
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

    public CourseCategory getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(CourseCategory courseCategory) {
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

    public String getAuthorIpAddress() {
        return authorIpAddress;
    }

    public void setAuthorIpAddress(String authorIpAddress) {
        this.authorIpAddress = authorIpAddress;
    }
}