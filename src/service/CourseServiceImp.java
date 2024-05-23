package service;
import exception.CourseNotFoundException;
import exception.NumValidatorException;
import exception.StringValidatorException;
import model.Course;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import repository.CourseRepository;
import view.Color;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CourseServiceImp implements CourseService, Color {
    Scanner sc = new Scanner(System.in);

    private void validateString(String input) throws StringValidatorException {
        if (input.matches(".*\\d.*")) {
            throw new StringValidatorException(RED + "⚠️ Invalid input. Numbers are not allowed." + RESET);
        }
    }

    @Override
    public void addNewCourse() {
        String title = "";
        String[] instructors;
        String[] requirements;
        System.out.println("+" + "~".repeat(117) + "+");

        while (true) {
            try {
                System.out.print("[+] Enter Course Title: ");
                title = sc.nextLine();
                validateString(title);
                break;
            } catch (StringValidatorException e) {
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            try {
                System.out.print("[+] Enter Instructors: ");
                instructors = sc.nextLine().split(",");
                for (String instructor : instructors) {
                    validateString(instructor.trim());
                }
                break;
            } catch (StringValidatorException e) {
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            try {
                System.out.print("[+] Enter Requirements: ");
                requirements = sc.nextLine().split(",");
                for (String requirement : requirements) {
                    validateString(requirement.trim());
                }
                break;
            } catch (StringValidatorException e) {
                System.out.println(e.getMessage());
            }
        }

        int id = CourseRepository.generateUniqueId();
        Date startDate = new Date();

        Course newCourse = new Course(id, title, instructors, requirements, startDate);
        CourseRepository.addCourse(newCourse);

        System.out.println("+" + "~".repeat(117) + "+");
        System.out.println(GREEN + "✅ New course added successfully!" + RESET);
        System.out.println("+" + "~".repeat(117) + "+");
    }

    @Override
    public void listAllCourses() {
        List<Course> courses = CourseRepository.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("+" + "~".repeat(117) + "+");
            System.out.println(RED + "⚠️ No courses available." + RESET);
            System.out.println("+" + "~".repeat(117) + "+");
        } else {
            System.out.println("+" + "~".repeat(117) + "+");
            System.out.println("[+] Display All Data: ");
            System.out.println("+" + "~".repeat(117) + "+");
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
                table.addCell(course.getTitle(), cellStyle);
                table.addCell(String.join(",", course.getInstructorName()), cellStyle);
                table.addCell(String.join(",", course.getRequirement()), cellStyle);
                table.addCell(course.getStartDate().toString(), cellStyle);
            }
            System.out.println(table.render());
        }
    }

    private int validateCourseId(String input) throws NumValidatorException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("+" + "~".repeat(117) + "+");
            throw new NumValidatorException(RED + "⚠️ Invalid input. Please enter a valid course ID." + RESET);
        }
    }

    @Override
    public void findCourseById() throws NumValidatorException {
        System.out.println("+" + "~".repeat(117) + "+");
        System.out.print("[+] Enter Course ID to find: ");
        int courseId = validateCourseId(sc.nextLine());
        System.out.println("+" + "~".repeat(117) + "+");

        Optional<Course> foundCourse = CourseRepository.getCourseById(courseId);

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

            System.out.println("🔎 Course Found:");
            System.out.println("+" + "~".repeat(117) + "+");
            System.out.println(table.render());
        } else {
            System.out.println("+" + "~".repeat(117) + "+");
            System.out.println(RED + "⚠️ Not found course with ID " + courseId + RESET);
            System.out.println("+" + "~".repeat(117) + "+");
        }
    }

    @Override
    public void findCourseByTitle() {
        try {
            System.out.println("+" + "~".repeat(117) + "+");
            System.out.print("[+] Enter Course Title to find: ");
            String titleToFind = validateTitle(sc.nextLine());
            System.out.println("+" + "~".repeat(117) + "+");

            List<Course> foundCourses = CourseRepository.getCoursesByTitle(titleToFind);

            if (!foundCourses.isEmpty()) {
                System.out.println("+" + "~".repeat(117) + "+");
                System.out.println("🔎 Courses Found:");
                System.out.println("+" + "~".repeat(117) + "+");

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

                for (Course course : foundCourses) {
                    table.addCell(course.getId().toString(), cellStyle);
                    table.addCell(course.getTitle(), cellStyle);
                    table.addCell(String.join(",", course.getInstructorName()), cellStyle);
                    table.addCell(String.join(",", course.getRequirement()), cellStyle);
                    table.addCell(course.getStartDate().toString(), cellStyle);
                }

                System.out.println(table.render());
            } else {
                System.out.println("+" + "~".repeat(117) + "+");
                System.out.println(RED + "⚠️ No courses found with the title: " + titleToFind + RESET);
                System.out.println("+" + "~".repeat(117) + "+");
            }
        } catch (StringValidatorException e) {
            System.out.println(e.getMessage());
            System.out.println("+" + "~".repeat(117) + "+");
        }
    }

    private String validateTitle(String input) throws StringValidatorException {
        if (!input.matches("[a-zA-Z\\s]+")) {
            System.out.println("+" + "~".repeat(117) + "+");
            throw new StringValidatorException(RED + "⚠️ Invalid input. Numbers are not allowed." + RESET);
        }
        return input;
    }

   /* public void removeCourseById() {
        try {
            System.out.println("+" + "~".repeat(117) + "+");
            System.out.print("[+] Enter Course ID to remove: ");
            int courseId = validateCourseId(sc.nextLine());
            System.out.println("+" + "~".repeat(117) + "+");

            CourseRepository.removeCourseById(courseId);
            System.out.println("+" + "~".repeat(117) + "+");
            System.out.println(GREEN + "✅ Course has been removed successfully." + RESET);
            System.out.println("+" + "~".repeat(117) + "+");
        } catch (NumValidatorException e) {
            System.out.println(e.getMessage());
            System.out.println("+" + "~".repeat(117) + "+");
        } catch (CourseNotFoundException e) {
            System.out.println("+" + "~".repeat(117) + "+");
            System.out.println(RED + "⚠️ " + e.getMessage() + RESET);
            System.out.println("+" + "~".repeat(117) + "+");
        }
    }
}

    */

    public void removeCourseById() {
        try {
            System.out.println("+" + "~".repeat(117) + "+");
            System.out.print("[+] Enter Course ID to remove: ");
            int courseId = validateCourseId(sc.nextLine());
            System.out.println("+" + "~".repeat(117) + "+");

            Optional<Course> courseOptional = CourseRepository.getCourseById(courseId);

            if (courseOptional.isPresent()) {
                Course course = courseOptional.get();

                System.out.println("Course to be deleted:");
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

                table.addCell(course.getId().toString(), cellStyle);
                table.addCell(course.getTitle(), cellStyle);
                table.addCell(String.join(", ", course.getInstructorName()), cellStyle);
                table.addCell(String.join(", ", course.getRequirement()), cellStyle);
                table.addCell(course.getStartDate().toString(), cellStyle);

                System.out.println(table.render());

                System.out.print("Are you sure you want to delete this course? (yes/no): ");
                String confirmation = sc.nextLine().trim().toLowerCase();

                if (confirmation.equals("yes") || confirmation.equals("y")) {
                    CourseRepository.removeCourseById(courseId);
                    System.out.println("+" + "~".repeat(117) + "+");
                    System.out.println(GREEN + "✅ Course has been removed successfully." + RESET);
                    System.out.println("+" + "~".repeat(117) + "+");
                } else {
                    System.out.println("+" + "~".repeat(117) + "+");
                    System.out.println(YELLOW + "⚠️ Course deletion cancelled." + RESET);
                    System.out.println("+" + "~".repeat(117) + "+");
                }
            } else {
                System.out.println("+" + "~".repeat(117) + "+");
                System.out.println(RED + "⚠️ No course found with ID " + courseId + RESET);
                System.out.println("+" + "~".repeat(117) + "+");
            }
        } catch (NumValidatorException e) {
            System.out.println(e.getMessage());
            System.out.println("+" + "~".repeat(117) + "+");
        } catch (CourseNotFoundException e) {
            System.out.println("+" + "~".repeat(117) + "+");
            System.out.println(RED + "⚠️ " + e.getMessage() + RESET);
            System.out.println("+" + "~".repeat(117) + "+");
        }
    }

}