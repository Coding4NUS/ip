package nusgpt;

/**
 * Represents a task and contains its information
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructor for task class
     * @param description description of the task
     */
    public Task(String description) {
        assert description != null : "task description must not be null";
        this.description = description;
        this.isDone = false;
    }

    /**
     * Gives the status of the task if it is done
     * @return String icon represents status if task is done
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Gives the description of the task
     * @return String description of the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Marks the task as done
     */
    public void markDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done
     */
    public void markNotDone() {
        this.isDone = false;
    }

    /**
     * Shows if the task is done
     * @return boolean if the task is done
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * The string representation of the task
     * @return String task in string format
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * The data storage file representation of the task
     * @return String task in data storage format
     */
    public String toFileString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}