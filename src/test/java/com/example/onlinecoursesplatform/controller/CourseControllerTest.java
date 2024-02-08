
package com.example.onlinecoursesplatform.controller;

import com.example.onlinecoursesplatform.dataTrasferObject.CourseCategoryDTO;
import com.example.onlinecoursesplatform.dataTrasferObject.CourseDTO;
import com.example.onlinecoursesplatform.exceptions.course.CourseInvalidException;
import com.example.onlinecoursesplatform.exceptions.course.CourseNotFoundException;
import com.example.onlinecoursesplatform.service.CourseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTest {

    @MockBean
    private CourseServiceImpl courseService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/courses/1 - Found")
    @WithMockUser(username = "admin", password = "admin", authorities = "Administrator")
    void testGetCourseById() throws Exception {
        CourseDTO mockedCourseDTO = initializeCourseDTO();

        doReturn(mockedCourseDTO).when(courseService).getCourseById(1L);

        mockMvc.perform(get("/api/courses/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.courseTitle").value("Test Course Title"))
                .andExpect(jsonPath("$.courseContent").value("Test Course Content"))
                .andExpect(jsonPath("$.courseCategory.categoryId").value(1))
                .andExpect(jsonPath("$.courseCategory.categoryName").value("Test Category Name"))
                .andExpect(jsonPath("$.courseAuthor").value("Test Course Author"));
    }

    @Test
    @DisplayName("GET /api/courses/2 - Not Found")
    @WithMockUser(username = "admin", password = "admin", authorities = "Administrator")
    void testGetCourseByIdNotFound() throws Exception {
        doThrow(new CourseNotFoundException("Course not found with ID: 2")).when(courseService).getCourseById(2L);

        mockMvc.perform(get("/api/courses/{id}", 2))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("DELETE /api/courses/delete/1 - Successful")
    @WithMockUser(username = "admin", password = "admin", authorities = "Administrator")
    void testDeleteCoursesSuccessful() throws Exception {
        mockMvc.perform(delete("/api/courses/delete/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/courses/delete/2 - Not Found")
    @WithMockUser(username = "admin", password = "admin", authorities = "Administrator")
    void testDeleteCoursesNotFound() throws Exception {
        doThrow(new CourseNotFoundException("Course not found with ID: 2")).when(courseService).deleteCourse(2L);

        mockMvc.perform(delete("/api/courses/delete/{id}", 2))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/course/post - Successful")
    @WithMockUser(username = "admin", password = "admin", authorities = "Administrator")
    void testAddCourseSuccess() throws Exception {
        CourseDTO mockedCourseDTO = initializeCourseDTO();

        UserDetails userDetails = mock(UserDetails.class);

        doReturn(mockedCourseDTO).when(courseService).addCourse(any(),eq(userDetails));

        mockMvc.perform(post("/api/courses/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockedCourseDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.courseTitle").value("Test Course Title"))
                .andExpect(jsonPath("$.courseContent").value("Test Course Content"))
                .andExpect(jsonPath("$.courseCategory.categoryId").value(1))
                .andExpect(jsonPath("$.courseCategory.categoryName").value("Test Category Name"))
                .andExpect(jsonPath("$.courseAuthor").value("Test Course Author"));
    }


    @Test
    @DisplayName("POST /api/courses/post - Bad Request")
    @WithMockUser(username = "admin", password = "admin", authorities = "Administrator")
    void testAddCourseBadRequest() throws Exception {
        CourseDTO mockedCourseDTO = initializeCourseDTO();

        UserDetails userDetails = mock(UserDetails.class);

        doThrow(new CourseInvalidException("A course with the same title already exists.")).when(courseService).addCourse(any(),eq(userDetails));

        mockMvc.perform(post("/api/courses/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockedCourseDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("PUT /api/courses/modify/1 - Successful")
    @WithMockUser(username = "admin", password = "admin", authorities = "Administrator")
    void testUpdateCourseSuccess() throws Exception {
        CourseDTO mockedCourseDTO = initializeCourseDTO();

        UserDetails userDetails = mock(UserDetails.class);

        doReturn(mockedCourseDTO).when(courseService).updateCourse(eq(1L), any(), eq(userDetails));

        mockMvc.perform(put("/api/courses/modify/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockedCourseDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.courseTitle").value("Test Course Title"))
                .andExpect(jsonPath("$.courseContent").value("Test Course Content"))
                .andExpect(jsonPath("$.courseCategory.categoryId").value(1))
                .andExpect(jsonPath("$.courseCategory.categoryName").value("Test Category Name"))
                .andExpect(jsonPath("$.courseAuthor").value("Test Course Author"));
    }


    @Test
    @DisplayName("PUT /api/courses/modify/1 - Not Found")
    @WithMockUser(username = "admin", password = "admin", authorities = "Administrator")
    void testUpdateCourseNotFound() throws Exception {
        CourseDTO mockedCourseDTO = initializeCourseDTO();

        UserDetails userDetails = mock(UserDetails.class);

        doThrow(new CourseNotFoundException("Course not found with ID: 1")).when(courseService).updateCourse(eq(1L), any(), eq(userDetails));

        mockMvc.perform(put("/api/courses/modify/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockedCourseDTO)))
                .andExpect(status().isNotFound());
    }


    private CourseDTO initializeCourseDTO() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseId(1L);
        courseDTO.setCourseTitle("Test Course Title");
        courseDTO.setCourseContent("Test Course Content");
        courseDTO.setCourseCategory(initializeCourseCategoryDTO());
        courseDTO.setCourseDate(new Date());
        courseDTO.setCourseAuthor("Test Course Author");

        return courseDTO;
    }

    private CourseCategoryDTO initializeCourseCategoryDTO() {
        CourseCategoryDTO courseCategoryDTO = new CourseCategoryDTO();
        courseCategoryDTO.setCategoryId(1L);
        courseCategoryDTO.setCategoryName("Test Category Name");

        return courseCategoryDTO;
    }

    private String asJsonString(final Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            throw new RuntimeException("Error converting object to JSON", ex);
        }
    }

}