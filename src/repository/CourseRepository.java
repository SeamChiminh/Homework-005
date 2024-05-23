package repository;
import exception.CourseNotFoundException;
import model.Course;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CourseRepository {

    private static List<Course> courses = new ArrayList<>();

    public static void addCourse(Course course) {
        courses.add(course);
    }

    public static List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }

    public static Optional<Course> getCourseById(int id) {
        return courses.stream().filter(course -> course.getId().equals(id)).findFirst();
    }

    public static List<Course> getCoursesByTitle(String title) {
        String titleLower = title.toLowerCase();
        return courses.stream()
                .filter(course -> course.getTitle().toLowerCase().contains(titleLower))
                .collect(Collectors.toList());
    }

    public static void removeCourseById(int id) throws CourseNotFoundException {
        Course course = getCourseById(id).orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + id));
        courses.remove(course);
    }

    public static int generateUniqueId() {
        int id;
        boolean unique;
        do {
            id = ThreadLocalRandom.current().nextInt(1000, 9999);
            int finalId = id;
            unique = courses.stream().noneMatch(course -> course.getId().equals(finalId));
        } while (!unique);
        return id;
    }
}

