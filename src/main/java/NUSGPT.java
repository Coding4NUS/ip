import java.util.Scanner;

public class NUSGPT {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // horizontal line template
        String horizontal_line = "____________________________________________________________\n";

        // print greeting
        System.out.println(horizontal_line
                + " Hello! I'm NUSGPT\n"
                + " What can I do for you?\n"
                + horizontal_line);

        // detect user commands
        String command = scanner.nextLine();

        // if the command is not bye echo
        while (!command.equals("bye")) {
            System.out.println(horizontal_line
                    + " " + command + "\n"
                    + horizontal_line);

            // let the user input a new command
            command = scanner.nextLine();
        }

        // if the command is bye exit
        System.out.println(horizontal_line
                + " Bye. Hope to see you again soon!\n"
                + horizontal_line);
        scanner.close();
    }
}