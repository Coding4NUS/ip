package nusgpt;

/**
 * Represents a simple todo task without date/time.
 */
// generated JavaDoc comment using ChatGPT
public class ToDo extends Task {
    /**
     * Creates a todo task.
     *
     * @param description Task description.
     */
    // generated JavaDoc comment using ChatGPT
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns user-facing string representation.
     *
     * @return Display string.
     */
    // generated JavaDoc comment using ChatGPT
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns storage representation.
     *
     * @return Storage line.
     */
    // generated JavaDoc comment using ChatGPT
    @Override
    public String toFileString() {
        return "T | " + (isDone() ? "1" : "0") + " | " + getDescription();
    }
}
