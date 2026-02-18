package nusgpt;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an in-memory list of tasks and related operations.
 */
// generated JavaDoc comment using ChatGPT
public class TaskList {
    // list of tasks
    private final ArrayList<Task> tasks;

    // constructor for no argument makes an empty task list
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list initialized from an existing list.
     *
     * @param initial Initial tasks (non-null).
     */
    // generated JavaDoc comment using ChatGPT
    public TaskList(List<Task> initial) {
        assert initial != null : "initial task list must not be null";
        this.tasks = new ArrayList<>(initial);
    }

    /**
     * Returns number of tasks.
     *
     * @return Size of list.
     */
    // generated JavaDoc comment using ChatGPT
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the given zero-based index.
     *
     * @param index Zero-based index.
     * @return Task at index.
     */
    // generated JavaDoc comment using ChatGPT
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "index must be within [0, size)";
        return tasks.get(index);
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     */
    // generated JavaDoc comment using ChatGPT
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given zero-based index.
     *
     * @param indexZeroBased Zero-based index.
     * @return Removed task.
     */
    // generated JavaDoc comment using ChatGPT
    public Task remove(int indexZeroBased) {
        assert indexZeroBased >= 0 && indexZeroBased < tasks.size() : "index must be within [0, size)";
        return tasks.remove(indexZeroBased);
    }

    /**
     * Returns the underlying list for storage saving.
     *
     * @return Backing ArrayList.
     */
    // generated JavaDoc comment using ChatGPT
    public ArrayList<Task> taskArrayList() {
        return tasks;
    }

    /**
     * Returns array list of tasks with descriptions that match the keyword
     *
     * @param keyword word user is searching for
     * @return ArrayList<Task> array list of tasks with keyword in description
     */
    public ArrayList<Task> findByKeyword(String keyword) {
        assert keyword != null : "keyword must not be null";
        final ArrayList<Task> matches = new ArrayList<>();
        final String word = keyword.trim().toLowerCase();

        for (Task t : tasks) {
            final String description = t.getDescription().toLowerCase();
            if (description.contains(word)) {
                matches.add(t);
            }
        }
        return matches;
    }
}
