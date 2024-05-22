import exception.NumValidatorException;
import service.CourseServiceImp;
import view.Color;
import view.Menu;

import java.util.Scanner;

public class Main implements Color {
    private static int validateOption(String input) throws NumValidatorException {
        try {
            int option = Integer.parseInt(input);
            return option;
        } catch (NumberFormatException e) {
            throw new NumValidatorException( RED + "⚠️ Invalid input. Please enter a valid option number." + RESET);
        }
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        CourseServiceImp courseServiceImp = new CourseServiceImp();
        boolean exit = false;

        while (!exit)
        {
            Menu.displayMenu();
            System.out.print("[+] Insert option: ");
            try{
                Integer option = validateOption(sc.nextLine());
                switch (option) {
                    case 1:

                        courseServiceImp.addNewCourse();
                        break;
                    case 2:

                        courseServiceImp.listAllCourses();
                        break;
                    case 3:

                        courseServiceImp.findCourseById();
                        break;
                    case 4:

                        courseServiceImp.findCourseByTitle();
                        break;
                    case 5:

                        courseServiceImp.removeCourseById();
                        break;
                    case 0,99:
                        System.out.println("Exit program...");
                        exit = true;
                        break;
                    default:
                        System.out.println(RED + "⚠️ Invalid input, option must be between 0 and 5." + RESET);
                }
            }catch (NumValidatorException e) {
                System.out.println(e.getMessage());
            }
        }
        sc.close();
    }
}