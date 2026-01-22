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
            // check if the task is a todo task
            } else if (command.startsWith("todo")) {
                // get description of todo from input
                String description = command.substring(5);
                // check if there is space in the task list
                if (taskNumber < maxTasks) {
                    // add the todo task into the task list
                    tasks[taskNumber] = new ToDo(description);
                    System.out.println(horizontal_line
                            + "Got it. I've added this task:\n"
                            + tasks[taskNumber] + "\n"
                            + "Now you have " + (taskNumber + 1) + " tasks in the list.\n"
                            + horizontal_line);
                    taskNumber++;
                }
            // check if the task is a deadline task
            } else if (command.startsWith("deadline")) {
                // get information of the deadline task from the input
                String taskInfo = command.substring(9);
                // the date of the deadline is after the " /by " text
                int dateIndex = taskInfo.indexOf(" /by ");
                // get the description of the deadline task from the input
                String description = taskInfo.substring(0, dateIndex);
                // get the date of the deadline task from the input
                String date = taskInfo.substring(dateIndex + 5);
                // check if there is space in the task list
                if (taskNumber < maxTasks) {
                    // add the deadline task into the task list
                    tasks[taskNumber] = new Deadline(description, date);
                    System.out.println(horizontal_line
                            + "Got it. I've added this task:\n"
                            + tasks[taskNumber] + "\n"
                            + "Now you have " + (taskNumber + 1) + " tasks in the list.\n"
                            + horizontal_line);
                    taskNumber++;
                }
            // check if the task is an event task
            } else if (command.startsWith("event")) {
                // get information of the event task from the input
                String taskInfo = command.substring(6);
                // the start of the event is after the " /from " text
                int startIndex = taskInfo.indexOf(" /from ");
                // the end of the event is after the " /to " text
                int endIndex = taskInfo.indexOf(" /to ");
                // get the description of the event task from the input
                String description = taskInfo.substring(0, startIndex);
                // get the start of the deadline task from the input
                String start = taskInfo.substring(startIndex + 7, endIndex);
                // get the end of the deadline task from the input
                String end = taskInfo.substring(endIndex + 5);
                // check if there is space in the task list
                if (taskNumber < maxTasks) {
                    tasks[taskNumber] = new Event(description, start, end);
                    System.out.println(horizontal_line
                            + "Got it. I've added this task:\n"
                            + tasks[taskNumber] + "\n"
                            + "Now you have " + (taskNumber + 1) + " tasks in the list.\n"
                            + horizontal_line);
                    taskNumber++;
                }
            } else {
                // store the text in the list if it has space
                if (taskNumber < maxTasks) {
                    // add the event task into the task list
                    tasks[taskNumber] = new Task(command);
                    System.out.println(horizontal_line
                            + "Got it. I've added this task:\n"
                            + tasks[taskNumber] + "\n"
                            + "Now you have " + (taskNumber + 1) + " tasks in the list.\n"
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