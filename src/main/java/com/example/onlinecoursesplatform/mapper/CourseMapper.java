package com.example.onlinecoursesplatform.mapper;

import com.example.onlinecoursesplatform.dataTrasferObject.CourseCategoryDTO;
import com.example.onlinecoursesplatform.dataTrasferObject.CourseDTO;
import com.example.onlinecoursesplatform.model.Course;
import com.example.onlinecoursesplatform.model.CourseCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseCategoryDTO courseCategoryToDTO(CourseCategory courseCategory);

    CourseCategory courseCategoryDTOToCourseCategory(CourseCategoryDTO courseCategoryDTO);

    CourseDTO courseToCourseDTO(Course course);

    Course courseDTOToCourse(CourseDTO courseDTO);

    List<CourseDTO> courseListToCourseDTOList(List<Course> courses);
}
