import java.util.ArrayList;
import java.util.List;

public class TaskList {
    // list of tasks
    private final ArrayList<Task> tasks;

    // constructor for no argument makes an empty task list
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    // constructor for given task list copies it to new task list
    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    // return number of tasks in list
    public int size() {
        return tasks.size();
    }

    // returns the task from the index in task list
    public Task get(int index) {
        return tasks.get(index);
    }

    // adds task to task list
    public void add(Task task) {
        tasks.add(task);
    }

    // removes the task from the index in task list
    public Task remove(int indexZeroBased) {
        return tasks.remove(indexZeroBased);
    }

    // return the task array list for saving
    public ArrayList<Task> taskArrayList() {
        return tasks;
    }
}
