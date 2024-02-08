package com.example.onlinecoursesplatform.service;

import com.example.onlinecoursesplatform.dataTrasferObject.CourseDTO;
import com.example.onlinecoursesplatform.exceptions.course.CourseInvalidException;
import com.example.onlinecoursesplatform.exceptions.course.CourseNotFoundException;
import com.example.onlinecoursesplatform.exceptions.course.CourseServiceException;
import com.example.onlinecoursesplatform.exceptions.ipAddress.IpAddressFetchException;
import com.example.onlinecoursesplatform.mapper.CourseMapper;
import com.example.onlinecoursesplatform.model.Course;
import com.example.onlinecoursesplatform.model.CourseCategory;
import com.example.onlinecoursesplatform.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseCategoryService courseCategoryService;

    @Autowired
    private IpAddressService authorIPAddressClient;

    private static final Logger logger = Logger.getLogger(CourseServiceImpl.class.getName());

    @Override
    public CourseDTO getCourseById(Long courseId) throws CourseNotFoundException {
        return courseRepository.findById(courseId)
                .map(courseMapper::courseToCourseDTO)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + courseId));
    }

    @Override
    public CourseDTO updateCourse(Long courseId, CourseDTO updatedCourseDTO, UserDetails userDetails)
            throws CourseNotFoundException, CourseInvalidException, IpAddressFetchException, CourseServiceException {
        try {
            Course existingCourse = courseRepository.findById(courseId)
                    .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + courseId));

            String newTitle = updatedCourseDTO.getCourseTitle();
            if (!existingCourse.getCourseTitle().equals(newTitle) && courseRepository.findByCourseTitle(newTitle).isPresent()) {
                throw new CourseInvalidException("Course with title '" + newTitle + "' already exists.");
            }

            String currentUsername = userDetails.getUsername();
            existingCourse.setCourseAuthor(currentUsername);

            Long newCategoryId = updatedCourseDTO.getCourseCategory().getCategoryId();
            CourseCategory newCategory = courseCategoryService.findCategoryById(newCategoryId);
            existingCourse.setCourseCategory(newCategory);

            existingCourse.setCourseTitle(newTitle);
            existingCourse.setCourseContent(updatedCourseDTO.getCourseContent());

            return getCourseAuthorIpAddress(existingCourse);
        } catch (CourseNotFoundException e) {
            logger.log(Level.WARNING, "Course not found while updating", e);
            throw e;
        } catch (CourseInvalidException e) {
            logger.log(Level.WARNING, "Invalid course data while updating", e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An unexpected error occurred while updating the course.", e);
            throw new CourseServiceException("An unexpected error occurred while updating the course.");
        }
    }

    @Override
    public void deleteCourse(Long courseId) throws CourseNotFoundException, CourseServiceException {
        try{
            if (courseRepository.findById(courseId).isPresent()) {
                courseRepository.deleteById(courseId);
            } else {
                throw new CourseNotFoundException("Course not found with ID: " + courseId + ", so it cannot be deleted");
            }
        }catch (CourseNotFoundException e) {
            logger.log(Level.WARNING, "Course not found while deleting the course.", e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An unexpected error occurred while deleting the course.", e);
            throw new CourseServiceException("An unexpected error occurred while deleting the course.");
        }
    }

    @Override
    public List<CourseDTO> findCoursesByTitleOrCategoryName(String titleKeyword, String categoryKeyword) {
        List<Course> courses = courseRepository.findCoursesByTitleOrCategoryName(titleKeyword, categoryKeyword);
        return courses.stream()
                .map(course -> courseMapper.courseToCourseDTO(course))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.courseListToCourseDTOList(courses);
    }

    @Override
    public CourseDTO addCourse(CourseDTO courseDTO, UserDetails userDetails) throws CourseInvalidException, IpAddressFetchException, CourseServiceException {
        try {
            Course course = courseMapper.courseDTOToCourse(courseDTO);

            if (courseRepository.findByCourseTitle(course.getCourseTitle()).isPresent()) {
                throw new CourseInvalidException("A course with the same title already exists.");
            }

            String currentUsername = userDetails.getUsername();
            course.setCourseAuthor(currentUsername);

            return getCourseAuthorIpAddress(course);
        } catch (CourseInvalidException e) {
            logger.log(Level.WARNING, "Invalid course data while creating the course.", e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An unexpected error occurred while creating the course.", e);
            throw new CourseServiceException("An unexpected error occurred while creating the course.");
        }
    }

    private CourseDTO getCourseAuthorIpAddress(Course course) throws IpAddressFetchException {
        String ipAddress;
        ipAddress = authorIPAddressClient.fetchUserIpAddress();

        course.setAuthorIpAddress(ipAddress);

        Course savedCourse = courseRepository.save(course);
        return courseMapper.courseToCourseDTO(savedCourse);
    }
}