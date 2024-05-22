import service.CourseServiceImp;
import view.Menu;

import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        CourseServiceImp courseServiceImp = new CourseServiceImp();
        boolean exit = false;


        while (!exit)
        {
            Menu.displayMenu();
            System.out.print("[+] Insert option: ");
            Integer option = sc.nextInt();

            try{
                switch (option) {
                    case 1:
                        System.out.println("1. add new course");
                        courseServiceImp.addNewCourse();
                        break;
                    case 2:
                        System.out.println("2. list all course");
                        courseServiceImp.listAllCourses();
                        break;
                    case 3:
                        System.out.println("3. find course by id");
                        courseServiceImp.findCourseById();
                        break;
                    case 4:
                        System.out.println("4. find course by title");
                        courseServiceImp.findCourseByTitle();
                        break;
                    case 5:
                        System.out.println("5. remove course by id");
                        courseServiceImp.removeCourseById();
                        break;
                    case 0,99:
                        System.out.println("Exit program...");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid input");
                }
            }catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        sc.close();
    }
}