package service;

import exception.NumValidatorException;

public interface CourseService {
    void addNewCourse();
    void listAllCourses();
    void findCourseById() throws NumValidatorException;
    void findCourseByTitle();
    void removeCourseById();
}
