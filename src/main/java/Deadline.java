import java.time.LocalDateTime;

public class Deadline extends Task {
    private LocalDateTime taskDateTime;
    private boolean HasTime;
    private String text;

    public Deadline(String description, String date) {
        super(description);
        checkDate(date);
    }

    // check date text to see if needed to parse
    private void checkDate(String string) {
        // check if date text is null or empty
        if (string == null) {
            text = "";
            taskDateTime = null;
            HasTime = false;
            return;
        }
        String date = string.trim();
        if (date.isEmpty()) {
            text = "";
            taskDateTime = null;
            HasTime = false;
            return;
        }
        // check if date text does not fit date format
        if (!DateTime.matchDateFormat(date)) {
            text = date;
            taskDateTime = null;
            HasTime = false;
            return;
        }
        // if date text fits format parse
        DateTime.ParsedDateTime parsed = DateTime.parseUserInput(date);
        text = null;
        taskDateTime = parsed.getDateTime();
        HasTime = parsed.hasTime();
    }

    public String getTime() {
        if (taskDateTime != null) {
            return DateTime.datePrintFormat(taskDateTime, HasTime);
        }
        return text;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + getTime() + ")";
    }

    @Override
    public String toFileString() {
        // store date in storage format
        String stored = (taskDateTime != null)
                ? DateTime.dateStorageFormat(taskDateTime, HasTime)
                : text;
        return "D | " + (isDone() ? "1" : "0") + " | " + getDescription() + " | " + stored;
    }
}
