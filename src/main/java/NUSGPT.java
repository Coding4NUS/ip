import java.util.Scanner;

public class NUSGPT {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // store up to 100 tasks
        int taskNumber = 0;
        int maxTasks = 100;
        String[] tasks = new String[maxTasks];

        // horizontal line template
        String horizontal_line = "____________________________________________________________\n";

        // print greeting
        System.out.println(horizontal_line
                + " Hello! I'm NUSGPT\n"
                + " What can I do for you?\n"
                + horizontal_line);

        // detect user commands
        String command = scanner.nextLine();

        // check if the command is not bye
        while (!command.equals("bye")) {
            // if the command is list show all the items in the list
            if (command.equals("list")) {
                System.out.println(horizontal_line);
                // for each item in the list print it in order
                for (int i = 0; i < taskNumber; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                System.out.println(horizontal_line);
            } else {
                // store the text in the list if it has space
                if (taskNumber < maxTasks) {
                    tasks[taskNumber] = command;
                    taskNumber++;
                    System.out.println(horizontal_line
                            + "added: " + command + "\n"
                            + horizontal_line);
                }
            }
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