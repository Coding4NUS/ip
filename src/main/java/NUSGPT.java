import java.util.Scanner;

public class NUSGPT {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // store up to 100 tasks
        int taskNumber = 0;
        int maxTasks = 100;
        Task[] tasks = new Task[maxTasks];

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
                System.out.println("Here are the tasks in your list:");
                // for each item in the list print it in order
                for (int i = 0; i < taskNumber; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                System.out.println(horizontal_line);
            // check if the command says to mark an item on the list
            } else if (command.startsWith("mark")) {
                // get the index of the item in the list to be marked
                int index = Integer.parseInt(command.substring(5));
                // check if the index is a valid number
                if (index < 1 || index > taskNumber) {
                    System.out.println(horizontal_line
                            + index + " is not a valid index\n"
                            + horizontal_line);
                } else {
                    // mark the item in the list index
                    Task task = tasks[index - 1];
                    task.markDone();
                    System.out.println(horizontal_line
                            + "Nice! I've marked this task as done:\n"
                            + task + "\n"
                            + horizontal_line);
                }
            // check if the command says to unmark an item on the list
            } else if (command.startsWith("unmark")) {
                // get the index of the item in the list to be unmarked
                int index = Integer.parseInt(command.substring(7));
                // check if the index is a valid number
                if (index < 1 || index > taskNumber) {
                    System.out.println(horizontal_line
                            + index + " is not a valid index\n"
                            + horizontal_line);
                } else {
                    // unmark the item in the list index
                    Task task = tasks[index - 1];
                    task.markNotDone();
                    System.out.println(horizontal_line
                            + "OK, I've marked this task as not done yet:\n"
                            + task + "\n"
                            + horizontal_line);
                }
            } else {
                // store the text in the list if it has space
                if (taskNumber < maxTasks) {
                    tasks[taskNumber] = new Task(command);
                    System.out.println(horizontal_line
                            + "added: " + tasks[taskNumber] + "\n"
                            + horizontal_line);
                    taskNumber++;
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