import java.util.Scanner;

public class Ui {
    private final Scanner scanner;
    // horizontal line template
    private static final String HORIZONTAL_LINE = "____________________________________________________________\n";

    // constructor for Ui class
    public Ui() {
        scanner = new Scanner(System.in);
    }

    // template for greeting
    public void showGreeting() {
        System.out.println(HORIZONTAL_LINE
                + " Hello! I'm NUSGPT\n"
                + " What can I do for you?\n"
                + HORIZONTAL_LINE);
    }

    // reads line of input from the user
    public String readCommand() {
        return scanner.nextLine();
    }

    // template for bye
    public void showBye() {
        System.out.println(HORIZONTAL_LINE
                + " Bye. Hope to see you again soon!\n"
                + HORIZONTAL_LINE);
    }

    // template for error from loading tasks from data storage
    public void showLoadingError() {
        System.out.println(HORIZONTAL_LINE
                + "error: could not load tasks from hard disk.\n"
                + HORIZONTAL_LINE);
    }

    // template for error from saving tasks to data storage
    public void showSaveError() {
        System.out.println(HORIZONTAL_LINE
                + "error: could not save tasks to hard disk.\n"
                + HORIZONTAL_LINE);
    }

    // template for errors
    public void showError(String message) {
        System.out.println(HORIZONTAL_LINE + message + HORIZONTAL_LINE);
    }

    // template for showing list of tasks
    public void showList(TaskList tasks) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("Here are the tasks in your list:");
        // for each item in the list print it in order
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println(HORIZONTAL_LINE);
    }

    // template for telling user task is added
    public void showTaskAdded(Task task, int size) {
        System.out.println(HORIZONTAL_LINE
                + "Got it. I've added this task:\n"
                + task + "\n"
                + "Now you have " + size + " tasks in the list.\n"
                + HORIZONTAL_LINE);
    }

    // template for telling user task is removed
    public void showTaskRemoved(Task task, int size) {
        System.out.println(HORIZONTAL_LINE
                + "Noted. I've removed this task:\n"
                + task + "\n"
                + "Now you have " + size + " tasks in the list.\n"
                + HORIZONTAL_LINE);
    }

    // template for telling user task is marked as done
    public void showTaskMarked(Task task) {
        System.out.println(HORIZONTAL_LINE
                + "Nice! I've marked this task as done:\n"
                + task + "\n"
                + HORIZONTAL_LINE);
    }

    // template for telling user task is unmarked as done
    public void showTaskUnmarked(Task task) {
        System.out.println(HORIZONTAL_LINE
                + "OK, I've marked this task as not done yet:\n"
                + task + "\n"
                + HORIZONTAL_LINE);
    }

    // close the scanner
    public void close() {
        scanner.close();
    }
}
