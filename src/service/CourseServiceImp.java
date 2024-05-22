package service;
import exception.NumValidatorException;
import exception.StringValidatorException;
import model.Course;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import view.Color;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CourseServiceImp implements CourseService , Color {
    Scanner sc = new Scanner(System.in);
    private static List<Course> courses = new ArrayList<>();

    private void validateString(String input) throws StringValidatorException {
        if (input.matches(".*\\d.*")) {
            throw new StringValidatorException( RED + "⚠️ Invalid input. Numbers are not allowed." + RESET);
        }
    }

    @Override
    public void addNewCourse() {
        String title = "";
        String[] instructors;
        String[] requirements;

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

        int id = generateUniqueId();
        Date startDate = new Date();

        Course newCourse = new Course(id, title, instructors, requirements, startDate);
        courses.add(newCourse);

        System.out.println(GREEN +"✅ New course added successfully!" + RESET);
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
            System.out.println(RED + "⚠️ No courses available." + RESET);
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
            int courseId = Integer.parseInt(input);
            return courseId;
        } catch (NumberFormatException e) {
            throw new NumValidatorException(RED + "⚠️ Invalid input. Please enter a valid course ID." + RESET);
        }
    }

    @Override
    public void findCourseById() throws NumValidatorException {
        System.out.print("[+] Enter Course ID to find: ");
        int courseId = validateCourseId(sc.nextLine());
//        sc.nextLine();
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

            System.out.println("🔎 Course Found:");
            System.out.println(table.render());
        } else {
            System.out.println( RED + "⚠️ Not found course with ID " + courseId + RESET);
        }
    }

    @Override
    public void findCourseByTitle() {
        try {
            System.out.print("[+] Enter Course Title to find: ");
            String titleToFind = validateTitle(sc.nextLine());
            titleToFind = titleToFind.trim();
            String titleToFindLower = titleToFind.toLowerCase();

            List<Course> foundCourses = courses.stream()
                    .filter(course -> course.getTitle().toLowerCase().contains(titleToFindLower))
                    .collect(Collectors.toList());

            if (!foundCourses.isEmpty()) {
                System.out.println("🔎 Courses Found:");

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
                System.out.println( RED + "⚠️ No courses found with the title: " + titleToFind + RESET);
            }
        } catch (StringValidatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private String validateTitle(String input) throws StringValidatorException {
        if (!input.matches("[a-zA-Z\\s]+")) {
            throw new StringValidatorException(RED + "⚠️ Invalid input. Numbers are not allowed." + RESET);
        }
        return input;
    }

    public void removeCourseById() {
        try {
            System.out.print("[+] Enter Course ID to remove: ");
            int courseId = validateCourseId(sc.nextLine());

            Optional<Course> foundCourse = courses.stream()
                    .filter(course -> course.getId().equals(courseId))
                    .findFirst();

            if (foundCourse.isPresent()) {
                courses.remove(foundCourse.get());
                System.out.println(GREEN + "✅ Course has been removed successfully." + courseId + RESET);
            } else {
                System.out.println(RED + "⚠️ Not found course with ID " + courseId + RESET);
            }
        } catch (NumValidatorException e) {
            System.out.println(e.getMessage());
        }
    }

}


