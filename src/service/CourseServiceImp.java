package service;

import model.Course;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CourseServiceImp implements CourseService{

    Scanner sc = new Scanner(System.in);
    private static List<Course> courses = new ArrayList<>();

    @Override
    public void addNewCourse() {
        try {

            System.out.print("Enter Course Title: ");
            String title = sc.nextLine();

            System.out.print("Enter Instructors (comma-separated): ");
            String[] instructors = sc.nextLine().split(",");

            System.out.print("Enter Requirements (comma-separated): ");
            String[] requirements = sc.nextLine().split(",");

            int id = generateUniqueId();
            Date startDate = new Date();

            Course newCourse = new Course(id, title, instructors, requirements, startDate);
            courses.add(newCourse);

            System.out.println("New course added successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Invalid input type. Please try again.");
            sc.next();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    private int generateUniqueId() {
        Integer id;
        boolean unique;
        do {
            id = ThreadLocalRandom.current().nextInt(1000, 9999);
            Integer finalId = id;
            unique = courses.stream().noneMatch(course -> course.getId().equals(finalId));
        } while (!unique);
        return id;
    }


    @Override
    public void listAllCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
        } else {
            Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.CENTER);
            table.setColumnWidth(0, 10, 30);
            table.setColumnWidth(1, 20, 30);
            table.setColumnWidth(2, 20, 30);
            table.setColumnWidth(3, 25, 30);
            table.setColumnWidth(4, 20, 30);

            table.addCell("ID", cellStyle);
            table.addCell("Title", cellStyle);
            table.addCell("Instructors", cellStyle);
            table.addCell("Requirements", cellStyle);
            table.addCell("Start Date", cellStyle);

            for (Course course : courses) {
                table.addCell(course.getId().toString(), cellStyle);
                table.addCell(course.getTitle(),cellStyle);
                table.addCell(String.join(",", course.getInstructorName()), cellStyle);
                table.addCell(String.join(",", course.getRequirement()), cellStyle);
                table.addCell(course.getStartDate().toString(),cellStyle);
            }
            System.out.println(table.render());
        }
    }

    @Override
    public void findCourseById() {
        System.out.print("Enter Course ID to find: ");
        int courseId = sc.nextInt();
        Optional<Course> foundCourse = courses.stream()
                .filter(course -> course.getId().equals(courseId))
                .findFirst();

        if (foundCourse.isPresent()) {
            Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.CENTER);
            table.setColumnWidth(0, 10, 30);
            table.setColumnWidth(1, 20, 30);
            table.setColumnWidth(2, 20, 30);
            table.setColumnWidth(3, 25, 30);
            table.setColumnWidth(4, 20, 30);

            table.addCell("ID", cellStyle);
            table.addCell("Title", cellStyle);
            table.addCell("Instructors", cellStyle);
            table.addCell("Requirements", cellStyle);
            table.addCell("Start Date", cellStyle);

            Course course = foundCourse.get();
            table.addCell(course.getId().toString(), cellStyle);
            table.addCell(course.getTitle(), cellStyle);
            table.addCell(String.join(",", course.getInstructorName()), cellStyle);
            table.addCell(String.join(",", course.getRequirement()), cellStyle);
            table.addCell(course.getStartDate().toString(), cellStyle);

            System.out.println("Course Found:");
            System.out.println(table.render());
        } else {
            System.out.println("Course with ID " + courseId + " not found.");
        }
    }

    @Override
    public void findCourseByTitle() {
        System.out.print("Enter Course Title to find: ");
        sc.nextLine(); // Consume newline
        String titleToFind = sc.nextLine();

        List<Course> foundCourses = courses.stream()
                .filter(course -> course.getTitle().contains(titleToFind))
                .collect(Collectors.toList());

        if (!foundCourses.isEmpty()) {
            System.out.println("Courses Found:");
            for (Course course : foundCourses) {
                System.out.println(course);
            }
        } else {
            System.out.println("No courses found with the title: " + titleToFind);
        }
    }

    @Override
    public void removeCourseById() {
        System.out.print("Enter Course ID to remove: ");
        int courseId = sc.nextInt();

        Optional<Course> foundCourse = courses.stream()
                .filter(course -> course.getId().equals(courseId))
                .findFirst();

        if (foundCourse.isPresent()) {
            courses.remove(foundCourse.get());
            System.out.println("Course with ID " + courseId + " removed successfully.");
        } else {
            System.out.println("Course with ID " + courseId + " not found.");
        }
    }
}


