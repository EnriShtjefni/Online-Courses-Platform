package com.example.onlinecoursesplatform.service;

import com.example.onlinecoursesplatform.dataTrasferObject.CourseCategoryDTO;
import com.example.onlinecoursesplatform.dataTrasferObject.CourseDTO;
import com.example.onlinecoursesplatform.exceptions.course.CourseInvalidException;
import com.example.onlinecoursesplatform.exceptions.course.CourseNotFoundException;
import com.example.onlinecoursesplatform.exceptions.course.CourseServiceException;
import com.example.onlinecoursesplatform.exceptions.ipAddress.IpAddressFetchException;
import com.example.onlinecoursesplatform.mapper.CourseMapper;
import com.example.onlinecoursesplatform.model.Course;
import com.example.onlinecoursesplatform.model.CourseCategory;
import com.example.onlinecoursesplatform.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private CourseMapper appMapper;

    @Mock
    private CourseCategoryService courseCategoryService;

    @Mock
    private IpAddressService authorIpAddress;

    @Test
    @DisplayName("Test getCourseById Success")
    void testGetCourseByIdSuccess() throws CourseNotFoundException {
        Long courseId = 1L;
        Course mockCourse = new Course();
        CourseDTO expectedCourseDTO = createMockCourseDTO(1L, "Testing", "Content 1");
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        when(appMapper.courseToCourseDTO(mockCourse)).thenReturn(expectedCourseDTO);

        CourseDTO returnedCourseDTO = courseService.getCourseById(courseId);

        assertNotNull(returnedCourseDTO, "Course was not found");
        assertEquals(expectedCourseDTO, returnedCourseDTO, "Courses should be the same");
    }

    @Test
    @DisplayName("Test getCourseById Not Found")
    void testGetCourseByIdNotFound() {
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(CourseServiceException.class, () -> courseService.getCourseById(courseId), "Course was found when it shouldn't be");
    }

    @Test
    @DisplayName("Test getAllCourses Success")
    void testGetAllCoursesSuccess() {
        Course mockCourse1 = createMockCourse(1L, "Test Course 1", "Content 1");
        Course mockCourse2 = createMockCourse(2L, "Test Course 2", "Content 2");
        List<Course> mockCourses = Arrays.asList(mockCourse1, mockCourse2);

        CourseDTO mockCourseDTO1 = createMockCourseDTO(1L, "Test Course 1", "Content 1");
        CourseDTO mockCourseDTO2 = createMockCourseDTO(2L, "Test Course 2", "Content 2");
        List<CourseDTO> expectedCourseDTOs = Arrays.asList(mockCourseDTO1, mockCourseDTO2);

        when(courseRepository.findAll()).thenReturn(mockCourses);
        when(appMapper.courseListToCourseDTOList(mockCourses)).thenReturn(expectedCourseDTOs);

        List<CourseDTO> returnedCourseDTOs = courseService.getAllCourses();

        assertEquals(expectedCourseDTOs, returnedCourseDTOs, "Courses should be the same");
        verify(courseRepository, times(1)).findAll();
        verify(appMapper, times(1)).courseListToCourseDTOList(mockCourses);
    }

    @Test
    public void testAddCourse() throws CourseServiceException, IpAddressFetchException, CourseInvalidException {
        CourseCategoryDTO categoryDTO = createMockCategoryDTO(1L, "Web Development");

        CourseDTO courseDTO = createMockCourseDTO(1L, "Test Course", "Course Content");
        courseDTO.setCourseCategory(categoryDTO);

        Course course = createMockCourse(courseDTO.getCourseId(), courseDTO.getCourseTitle(), courseDTO.getCourseContent());

        given(courseRepository.findByCourseTitle(course.getCourseTitle())).willReturn(Optional.empty());
        given(appMapper.courseDTOToCourse(courseDTO)).willReturn(course);
        given(appMapper.courseToCourseDTO(course)).willReturn(courseDTO);
        given(courseRepository.save(course)).willReturn(course);

        CourseDTO result = courseService.addCourse(courseDTO, setUpMockSecurityContext());

        assertEquals(courseDTO.getCourseId(), result.getCourseId());
        assertEquals(courseDTO.getCourseTitle(), result.getCourseTitle());
        assertEquals(courseDTO.getCourseContent(), result.getCourseContent());
        assertEquals(courseDTO.getCourseCategory().getCategoryId(), result.getCourseCategory().getCategoryId());
    }

    @Test
    public void testAddCourseWithExistingTitle() {
        CourseCategoryDTO categoryDTO = createMockCategoryDTO(1L, "Web Development");

        CourseDTO courseDTO = createMockCourseDTO(1L, "Test Course", "Course Content");
        courseDTO.setCourseCategory(categoryDTO);

        given(courseRepository.findByCourseTitle(courseDTO.getCourseTitle())).willReturn(Optional.of(createMockCourse(1L, "Test Course", "Existing Content")));

        assertThrows(CourseServiceException.class, () -> courseService.addCourse(courseDTO, setUpMockSecurityContext()), "Exception thrown for existing title");
    }

    @Test
    void testDeleteCourseSuccess() {
        Long courseIdToDelete = 1L;
        Course courseToDelete = new Course();

        when(courseRepository.findById(courseIdToDelete)).thenReturn(Optional.of(courseToDelete));

        assertDoesNotThrow(() -> courseService.deleteCourse(courseIdToDelete));
    }

    @Test
    void testDeleteCourseNotFound() {
        Long courseIdToDelete = 1L;

        when(courseRepository.findById(courseIdToDelete)).thenReturn(Optional.empty());

        assertThrows(CourseServiceException.class, () -> courseService.deleteCourse(courseIdToDelete));
    }

    @Test
    void testFindCoursesByTitleOrCategoryName() {
        String titleKeyword = "Java";
        String categoryKeyword = "Web";

        Course course1 = createMockCourse(1L, "Java", "Content 1");
        CourseCategory courseCategory1 = createMockCategory(1L, "Web");
        course1.setCourseCategory(courseCategory1);

        Course course2 = createMockCourse(2L, "Python", "Content 2");
        CourseCategory courseCategory2 = createMockCategory(2L, "Programming");
        course2.setCourseCategory(courseCategory2);

        when(courseRepository.findCoursesByTitleOrCategoryName(course1.getCourseTitle(), courseCategory1.getCategoryName()))
                .thenReturn(Collections.singletonList(course1));
        when(courseRepository.findCoursesByTitleOrCategoryName(course2.getCourseTitle(), courseCategory2.getCategoryName()))
                .thenReturn(Collections.singletonList(course2));

        List<CourseDTO> result = courseService.findCoursesByTitleOrCategoryName(titleKeyword, categoryKeyword);

        assertEquals(1, result.size());

        CourseDTO resultCourse = result.get(0);
        assertEquals(course1.getCourseTitle(), resultCourse.getCourseTitle());
        assertEquals(courseCategory1.getCategoryName(), resultCourse.getCourseCategory().getCategoryName());
    }

    @Test
    void testUpdateCourse() throws CourseNotFoundException, IpAddressFetchException, CourseServiceException, CourseInvalidException {
        Course existingCourse = createMockCourse(1L, "Existing Course", "Existing Content");
        CourseDTO updatedCourseDTO = createMockCourseDTO(1L, "Updated Course", "Updated Content");
        CourseCategory newCategory = createMockCategory(2L, "Category Name");
        CourseCategoryDTO newCategoryDTO = createMockCategoryDTO(2L, "Category Name");

        updatedCourseDTO.setCourseCategory(newCategoryDTO);

        given(courseCategoryService.findCategoryById(anyLong())).willReturn(newCategory);
        given(authorIpAddress.fetchUserIpAddress()).willReturn("127.0.0.1");
        given(courseRepository.findById(1L)).willReturn(Optional.of(existingCourse));
        given(courseRepository.save(any())).willReturn(existingCourse);
        given(appMapper.courseToCourseDTO(any())).willReturn(updatedCourseDTO);

        CourseDTO result = courseService.updateCourse(1L, updatedCourseDTO, setUpMockSecurityContext());

        assertEquals("Updated Course", result.getCourseTitle());
        assertEquals("Updated Content", result.getCourseContent());
        assertEquals("admin", result.getCourseAuthor());
        assertEquals(newCategory, result.getCourseCategory());
        assertEquals("127.0.0.1", existingCourse.getAuthorIpAddress());
    }

    private CourseDTO createMockCourseDTO(Long courseId, String title, String content) {
        CourseDTO mockCourseDTO = new CourseDTO();
        mockCourseDTO.setCourseId(courseId);
        mockCourseDTO.setCourseTitle(title);
        mockCourseDTO.setCourseContent(content);
        return mockCourseDTO;
    }

    private Course createMockCourse(Long courseId, String title, String content) {
        Course mockCourse = new Course();
        mockCourse.setCourseId(courseId);
        mockCourse.setCourseTitle(title);
        mockCourse.setCourseContent(content);
        return mockCourse;
    }

    private CourseCategoryDTO createMockCategoryDTO(Long categoryID, String categoryName) {
        CourseCategoryDTO mockCategoryDTO = new CourseCategoryDTO();
        mockCategoryDTO.setCategoryId(categoryID);
        mockCategoryDTO.setCategoryName(categoryName);
        return mockCategoryDTO;
    }

    private CourseCategory createMockCategory(Long categoryID, String categoryName) {
        CourseCategory mockCategory = new CourseCategory();
        mockCategory.setCategoryId(categoryID);
        mockCategory.setCategoryName(categoryName);
        return mockCategory;
    }

    @WithMockUser
    private UserDetails setUpMockSecurityContext() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("mockedUsername");
        when(securityContext.getAuthentication()).thenReturn(authentication);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        return userDetails;
    }

}

