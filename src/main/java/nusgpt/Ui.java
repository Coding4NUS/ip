package nusgpt;

import java.util.List;
import java.util.Scanner;

/**
 * Handles user interaction formatting and console I/O.
 */
// generated JavaDoc comment using ChatGPT
public class Ui {
    private final Scanner scanner;

    /**
     * Creates a UI instance using standard input.
     */
    // generated JavaDoc comment using ChatGPT
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns greeting text.
     *
     * @return Greeting string.
     */
    // generated JavaDoc comment using ChatGPT
    public String formatGreeting() {
        return " Hello! I'm NUSGPT\n" + " What can I do for you?\n";
    }

    /**
     * Returns bye text.
     *
     * @return Bye string.
     */
    // generated JavaDoc comment using ChatGPT
    public String formatBye() {
        return " Bye. Hope to see you again soon!\n";
    }

    /**
     * Returns storage save error text.
     *
     * @return Save error string.
     */
    // generated JavaDoc comment using ChatGPT
    public String formatSaveError() {
        return "error: could not save tasks to hard disk.\n";
    }

    /**
     * Returns the given error message (pass-through).
     *
     * @param message Error message.
     * @return Error message.
     */
    // generated JavaDoc comment using ChatGPT
    public String formatError(String message) {
        return message;
    }

    /**
     * Returns help text listing all commands and date formats.
     *
     * @return Help string.
     */
    // generated JavaDoc comment using ChatGPT
    public String formatHelp() {
        return "Available commands:\n"
                + "1) help\n"
                + "2) list\n"
                + "3) todo (description)\n"
                + "4) deadline (description) /by (date)\n"
                + "5) event (description) /from (start date) /to (end date)\n"
                + "   date formats:\n"
                + "     - yyyy/MM/dd\n"
                + "     - yyyy/MM/dd HHmm\n"
                + "     - dd/M/yyyy\n"
                + "     - dd/M/yyyy HHmm\n"
                + "6) mark (task index)\n"
                + "7) unmark (task index)\n"
                + "8) delete (task index)\n"
                + "9) find (keyword)\n"
                + "10) bye\n";
    }

    /**
     * Formats the full task list.
     *
     * @param tasks Task list (non-null).
     * @return Formatted list string.
     */
    // generated JavaDoc comment using ChatGPT
    public String formatList(TaskList tasks) {
        assert tasks != null : "tasks must not be null";
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Formats find results.
     *
     * @param matches Matching tasks (non-null).
     * @return Formatted results.
     */
    // generated JavaDoc comment using ChatGPT
    public String formatFindResults(List<Task> matches) {
        assert matches != null : "matches must not be null";
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matches.size(); i++) {
            sb.append(i + 1).append(". ").append(matches.get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Formats response for adding a task.
     *
     * @param task Added task.
     * @param size New total size.
     * @return Formatted string.
     */
    // generated JavaDoc comment using ChatGPT
    public String formatTaskAdded(Task task, int size) {
        return "Got it. I've added this task:\n"
                + task + "\n"
                + "Now you have " + size + " tasks in the list.\n";
    }

    /**
     * Formats response for removing a task.
     *
     * @param task Removed task.
     * @param size New total size.
     * @return Formatted string.
     */
    // generated JavaDoc comment using ChatGPT
    public String formatTaskRemoved(Task task, int size) {
        return "Noted. I've removed this task:\n"
                + task + "\n"
                + "Now you have " + size + " tasks in the list.\n";
    }

    /**
     * Formats response for marking a task done.
     *
     * @param task Marked task.
     * @return Formatted string.
     */
    // generated JavaDoc comment using ChatGPT
    public String formatTaskMarked(Task task) {
        return "Nice! I've marked this task as done:\n"
                + task + "\n";
    }

    /**
     * Formats response for unmarking a task.
     *
     * @param task Unmarked task.
     * @return Formatted string.
     */
    // generated JavaDoc comment using ChatGPT
    public String formatTaskUnmarked(Task task) {
        return "OK, I've marked this task as not done yet:\n"
                + task + "\n";
    }

    /**
     * Prints greeting to console.
     */
    // generated JavaDoc comment using ChatGPT
    public void showGreeting() {
        System.out.println(" Hello! I'm NUSGPT\n"
                + " What can I do for you?\n");
    }

    /**
     * Reads a line from standard input.
     *
     * @return User input line.
     */
    // generated JavaDoc comment using ChatGPT
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints bye message to console.
     */
    // generated JavaDoc comment using ChatGPT
    public void showBye() {
        System.out.println(" Bye. Hope to see you again soon!\n");
    }

    /**
     * Prints loading error to console.
     */
    // generated JavaDoc comment using ChatGPT
    public void showLoadingError() {
        System.out.println("error: could not load tasks from hard disk.\n");
    }

    /**
     * Prints saving error to console.
     */
    // generated JavaDoc comment using ChatGPT
    public void showSaveError() {
        System.out.println("error: could not save tasks to hard disk.\n");
    }

    /**
     * Prints an error to console.
     *
     * @param message Error message.
     */
    // generated JavaDoc comment using ChatGPT
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Prints help to console.
     */
    // generated JavaDoc comment using ChatGPT
    public void showHelp() {
        System.out.print(formatHelp());
    }

    /**
     * Prints the full task list to console.
     *
     * @param tasks Task list.
     */
    // generated JavaDoc comment using ChatGPT
    public void showList(TaskList tasks) {
        System.out.print(formatList(tasks));
    }

    /**
     * Shows list of tasks whose description matches keyword
     *
     * @param matches list of tasks with matching descriptions
     */
    // generated JavaDoc comment using ChatGPT
    public void showFindResults(List<Task> matches) {
        System.out.print(formatFindResults(matches));
    }

    /**
     * Prints task-added message to console.
     *
     * @param task Added task.
     * @param size New total size.
     */
    // generated JavaDoc comment using ChatGPT
    public void showTaskAdded(Task task, int size) {
        System.out.print(formatTaskAdded(task, size));
    }

    /**
     * Prints task-removed message to console.
     *
     * @param task Removed task.
     * @param size New total size.
     */
    // generated JavaDoc comment using ChatGPT
    public void showTaskRemoved(Task task, int size) {
        System.out.println("Noted. I've removed this task:\n"
                + task + "\n"
                + "Now you have " + size + " tasks in the list.\n");
    }

    /**
     * Prints task-marked message to console.
     *
     * @param task Marked task.
     */
    // generated JavaDoc comment using ChatGPT
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:\n"
                + task + "\n");
    }

    /**
     * Prints task-unmarked message to console.
     *
     * @param task Unmarked task.
     */
    // generated JavaDoc comment using ChatGPT
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:\n"
                + task + "\n");
    }

    /**
     * Closes underlying scanner.
     */
    // generated JavaDoc comment using ChatGPT
    public void close() {
        scanner.close();
    }
}
