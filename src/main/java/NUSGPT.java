import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class NUSGPT {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // store up to 100 tasks
        int maxTasks = 100;
        // stores and loads tasks from the disk
        Storage storage = new Storage();
        // list of tasks
        ArrayList<Task> tasks;

        // try to load from existing data file
        try {
            tasks = storage.load();
        } catch (IOException e) {
            // if existing data file does not existing make a new task list
            tasks = new ArrayList<>();
        }

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
            try {
                // if the command is list show all the items in the list
                if (command.equals("list")) {
                    System.out.println(horizontal_line);
                    System.out.println("Here are the tasks in your list:");
                    // for each item in the list print it in order
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + ". " + tasks.get(i));
                    }
                    System.out.println(horizontal_line);
                // check if the command says to mark an item on the list
                } else if (command.startsWith("mark")) {
                    // get the index of the item in the list to be marked
                    int index = Integer.parseInt(command.substring(5).trim());
                    // check if the index is a valid number
                    if (index < 1 || index > tasks.size()) {
                        throw new NUSGPTException(index + " is not a valid index\n");
                    } else {
                        // mark the item in the list index
                        Task task = tasks.get(index - 1);
                        task.markDone();
                        // save the task in data storage
                        storage.save(tasks);
                        // print message
                        System.out.println(horizontal_line
                                + "Nice! I've marked this task as done:\n"
                                + task + "\n"
                                + horizontal_line);
                    }
                // check if the command says to unmark an item on the list
                } else if (command.startsWith("unmark")) {
                    // get the index of the item in the list to be unmarked
                    int index = Integer.parseInt(command.substring(7).trim());
                    // check if the index is a valid number
                    if (index < 1 || index > tasks.size()) {
                        throw new NUSGPTException(index + " is not a valid index\n");
                    } else {
                        // unmark the item in the list index
                        Task task = tasks.get(index - 1);
                        task.markNotDone();
                        // save the task in data storage
                        storage.save(tasks);
                        // print message
                        System.out.println(horizontal_line
                                + "OK, I've marked this task as not done yet:\n"
                                + task + "\n"
                                + horizontal_line);
                    }
                // check if the command is to delete a task from the task list
                } else if (command.startsWith("delete")) {
                    // get the index of the item in the list to be deleted
                    int index = Integer.parseInt(command.substring(7).trim());
                    // check if the index is a valid number
                    if (index < 1 || index > tasks.size()) {
                        throw new NUSGPTException(index + " is not a valid index\n");
                    } else {
                        // delete the item in the list index
                        Task task = tasks.remove(index - 1);
                        // save the task in data storage
                        storage.save(tasks);
                        // print message
                        System.out.println(horizontal_line
                                + "Noted. I've removed this task:\n"
                                + task + "\n"
                                + "Now you have " + tasks.size() + " tasks in the list.\n"
                                + horizontal_line);
                    }
                // check if the task is a todo task
                } else if (command.startsWith("todo")) {
                    // get description of todo from input
                    String description = command.length() >= 5 ? command.substring(5).trim() : "";
                    // if the description is empty throw an error
                    if (description.isEmpty()) {
                        throw new NUSGPTException("please provide a description for the todo task.\n");
                    }
                    // check if there is space in the task list
                    if (tasks.size() < maxTasks) {
                        // add the todo task into the task list
                        Task task = new ToDo(description);
                        tasks.add(task);
                        // save the task in data storage
                        storage.save(tasks);
                        // print message
                        System.out.println(horizontal_line
                                + "Got it. I've added this task:\n"
                                + task + "\n"
                                + "Now you have " + tasks.size() + " tasks in the list.\n"
                                + horizontal_line);
                    } else {
                        // if there is no space in the task list throw an error
                        throw new NUSGPTException("no space for new tasks in task list.\n");
                    }
                // check if the task is a deadline task
                } else if (command.startsWith("deadline")) {
                    // the date of the deadline is after the " /by " text
                    int dateIndex = command.indexOf(" /by ");
                    // if there is no date index throw an error
                    if (dateIndex == -1) {
                        throw new NUSGPTException("use the format: deadline (description) /by (date)\n");
                    }
                    // get the description of the deadline task from the input
                    String description = command.substring(8, dateIndex).trim();
                    // get the date of the deadline task from the input
                    String date = command.substring(dateIndex + 5).trim();
                    // if the description is empty throw an error
                    if (description.isEmpty()) {
                        throw new NUSGPTException("please provide a description for the deadline task.\n");
                    }
                    if (date.isEmpty()) {
                        throw new NUSGPTException("please provide a date for the deadline task.\n");
                    }
                    // check if there is space in the task list
                    if (tasks.size() < maxTasks) {
                        // add the deadline task into the task list
                        Task task = new Deadline(description, date);
                        tasks.add(task);
                        // save the task in data storage
                        storage.save(tasks);
                        // print message
                        System.out.println(horizontal_line
                                + "Got it. I've added this task:\n"
                                + task + "\n"
                                + "Now you have " + tasks.size() + " tasks in the list.\n"
                                + horizontal_line);
                    } else {
                        // if there is no space in the task list throw an error
                        throw new NUSGPTException("no space for new tasks in task list.\n");
                    }
                // check if the task is an event task
                } else if (command.startsWith("event")) {
                    // get information of the event task from the input
                    String taskInfo = command.length() >= 6 ? command.substring(6) : "";
                    // the start of the event is after the " /from " text
                    int startIndex = taskInfo.indexOf(" /from ");
                    // the end of the event is after the " /to " text
                    int endIndex = taskInfo.indexOf(" /to ");
                    // if there is no start index throw an error
                    if (startIndex == -1) {
                        throw new NUSGPTException("invalid value for 'from' input for event task\n");
                    }
                    // if there is no start index throw an error
                    if (endIndex == -1) {
                        throw new NUSGPTException("invalid value for 'to' input for event task\n");
                    }
                    // if the format is invalid throw an error
                    if (endIndex < startIndex) {
                        throw new NUSGPTException("use the format: event (description) /from (start) /to (end)\n");
                    }
                    // get the description of the event task from the input
                    String description = taskInfo.substring(0, startIndex).trim();
                    // get the start of the deadline task from the input
                    String start = taskInfo.substring(startIndex + 7, endIndex).trim();
                    // get the end of the deadline task from the input
                    String end = taskInfo.substring(endIndex + 5).trim();
                    // if the description is empty throw an error
                    if (description.isEmpty()) {
                        throw new NUSGPTException("please provide a description for the event task.\n");
                    }
                    // if the start time is empty throw an error
                    if (start.isEmpty()) {
                        throw new NUSGPTException("please provide a time for the start of the event task.\n");
                    }
                    // if the end time is empty throw an error
                    if (end.isEmpty()) {
                        throw new NUSGPTException("please provide a time for the end of the event task.\n");
                    }
                    // check if there is space in the task list
                    if (tasks.size() < maxTasks) {
                        // add the event task into the task list
                        Task task = new Event(description, start, end);
                        tasks.add(task);
                        // save the task in data storage
                        storage.save(tasks);
                        // print message
                        System.out.println(horizontal_line
                                + "Got it. I've added this task:\n"
                                + task + "\n"
                                + "Now you have " + tasks.size() + " tasks in the list.\n"
                                + horizontal_line);
                    } else {
                        // if there is no space in the task list throw an error
                        throw new NUSGPTException("no space for new tasks in task list.\n");
                    }
                } else {
                    throw new NUSGPTException("unidentified instruction. the following tasks are valid: todo, event, deadline\n");
                }
            } catch (NUSGPTException exception) {
                // print exception message
                System.out.println(horizontal_line + exception.getMessage() + horizontal_line);
            } catch (IOException exception) {
                // if there was an error with data storage print error message
                System.out.println(horizontal_line
                        + "error: could not save tasks to hard disk.\n"
                        + horizontal_line);
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