public class Deadline extends Task {
    protected String time;

    public Deadline(String description, String time) {
        super(description);
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + time + ")";
    }

    @Override
    public String toFileString() {
        return "D | " + (isDone() ? "1" : "0") + " | " + getDescription() + " | " + getTime();
    }
}
