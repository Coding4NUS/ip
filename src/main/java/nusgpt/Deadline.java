package nusgpt;

import java.time.LocalDateTime;

/**
 * Represents a task with a deadline ("by" date/time).
 * The deadline may be stored as a parsed date/time or as free text if parsing fails.
 */
// generated JavaDoc comment using ChatGPT
public class Deadline extends Task {
    private LocalDateTime taskDateTime;
    private boolean hasTime;
    private String text;

    /**
     * Creates a deadline task.
     *
     * @param description Task description (non-null).
     * @param date        Deadline text; if parseable it is stored as a date/time, otherwise as free text.
     */
    // generated JavaDoc comment using ChatGPT
    public Deadline(String description, String date) {
        super(description);
        checkDate(date);
    }

    /**
     * Check if date text fits date format
     * If it fits parse the text into date format
     * If it does not fit make it text
     *
     * @param string date text
     */
    private void checkDate(String string) {
        // check if date text is null or empty
        if (string == null) {
            setAsText("");
            return;
        }
        String date = string.trim();
        if (date.isEmpty()) {
            setAsText("");
            return;
        }
        // check if date text does not fit date format
        if (!DateTime.matchDateFormat(date)) {
            setAsText(date);
            return;
        }
        // if date text fits format parse
        DateTime.ParsedDateTime parsed = DateTime.parseUserInput(date);

        this.text = "";
        this.taskDateTime = parsed.getDateTime();
        this.hasTime = parsed.hasTime();
      
        assert this.taskDateTime != null : "taskDateTime must be set after successful parse";
    }

    /**
     * Helper to keep invariants consistent
     *
     * @param string string to set text to
     */
    private void setAsText(String string) {
        this.text = string;
        this.taskDateTime = null;
        this.hasTime = false;
    }

    /**
     * String of date for deadline
     *
     * @return String date in either text or date/time format
     */
    public String getTime() {
        if (taskDateTime != null) {
            return DateTime.datePrintFormat(taskDateTime, hasTime);
        }
        return text;
    }

    /**
     * String format of deadline information
     *
     * @return String format for deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + getTime() + ")";
    }

    /**
     * Returns the storage string representation of this task.
     *
     * @return Storage line for this task.
     */
    // generated JavaDoc comment using ChatGPT
    @Override
    public String toFileString() {
        // store date in storage format
        String stored = (taskDateTime != null)
                ? DateTime.dateStorageFormat(taskDateTime, hasTime)
                : text;
        return "D | " + (isDone() ? "1" : "0") + " | " + getDescription() + " | " + stored;
    }
}
