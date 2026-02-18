package nusgpt;

import java.util.List;
import java.util.Scanner;

public class Ui {
    private final Scanner scanner;

    // constructor for nusgpt.Ui class
    public Ui() {
        scanner = new Scanner(System.in);
    }

    public String formatGreeting() {
        return " Hello! I'm NUSGPT\n" + " What can I do for you?\n";
    }

    public String formatBye() {
        return " Bye. Hope to see you again soon!\n";
    }

    public String formatLoadingError() {
        return "error: could not load tasks from hard disk.\n";
    }

    public String formatSaveError() {
        return "error: could not save tasks to hard disk.\n";
    }

    public String formatError(String message) {
        return message;
    }

    public String formatHelp() {
        return ""
                + "Available commands:\n"
                + "1) help\n"
                + "2) list\n"
                + "3) todo (description)\n"
                + "4) deadline (description) /by (date)\n"
                + "5) event (description) /from (start date) /to (end date)\n"
                + "   date formats:\n"
                + "     - yyyy-MM-dd\n"
                + "     - yyyy-MM-dd HHmm\n"
                + "     - d/M/yyyy\n"
                + "     - d/M/yyyy HHmm\n"
                + "6) mark (task index)\n"
                + "7) unmark (task index)\n"
                + "8) delete (task index)\n"
                + "9) find (keyword)\n"
                + "10) bye\n";
    }

    public String formatList(TaskList tasks) {
        assert tasks != null : "tasks must not be null";
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String formatFindResults(List<Task> matches) {
        assert matches != null : "matches must not be null";
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matches.size(); i++) {
            sb.append(i + 1).append(". ").append(matches.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String formatTaskAdded(Task task, int size) {
        return "Got it. I've added this task:\n"
                + task + "\n"
                + "Now you have " + size + " tasks in the list.\n";
    }

    public String formatTaskRemoved(Task task, int size) {
        return "Noted. I've removed this task:\n"
                + task + "\n"
                + "Now you have " + size + " tasks in the list.\n";
    }

    public String formatTaskMarked(Task task) {
        return "Nice! I've marked this task as done:\n"
                + task + "\n";
    }

    public String formatTaskUnmarked(Task task) {
        return "OK, I've marked this task as not done yet:\n"
                + task + "\n";
    }

    // template for greeting
    public void showGreeting() {
        System.out.println(" Hello! I'm NUSGPT\n"
                + " What can I do for you?\n");
    }

    // reads line of input from the user
    public String readCommand() {
        return scanner.nextLine();
    }

    // template for bye
    public void showBye() {
        System.out.println(" Bye. Hope to see you again soon!\n");
    }

    // template for error from loading tasks from data storage
    public void showLoadingError() {
        System.out.println("error: could not load tasks from hard disk.\n");
    }

    // template for error from saving tasks to data storage
    public void showSaveError() {
        System.out.println("error: could not save tasks to hard disk.\n");
    }

    // template for errors
    public void showError(String message) {
        System.out.println(message);
    }

    public void showHelp() {
        System.out.print(formatHelp());
    }

    // template for showing list of tasks
    public void showList(TaskList tasks) {
        System.out.print(formatList(tasks));
    }

    /**
     * Shows list of tasks whose description matches keyword
     *
     * @param matches list of tasks with matching descriptions
     */
    public void showFindResults(List<Task> matches) {
        System.out.print(formatFindResults(matches));
    }

    // template for telling user task is added
    public void showTaskAdded(Task task, int size) {
        System.out.print(formatTaskAdded(task, size));
    }

    // template for telling user task is removed
    public void showTaskRemoved(Task task, int size) {
        System.out.println("Noted. I've removed this task:\n"
                + task + "\n"
                + "Now you have " + size + " tasks in the list.\n");
    }

    // template for telling user task is marked as done
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:\n"
                + task + "\n");
    }

    // template for telling user task is unmarked as done
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:\n"
                + task + "\n");
    }

    // close the scanner
    public void close() {
        scanner.close();
    }
}
