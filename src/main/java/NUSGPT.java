import java.io.IOException;
import java.util.ArrayList;

public class NUSGPT {
    // store up to 100 tasks
    private static final int MAX_TASKS = 100;
    // stores and loads tasks from the disk
    private final Storage storage;
    // list of tasks
    private final TaskList tasks;
    // templates for user interaction
    private final Ui ui;

    // constructor for NUSGPT class
    public NUSGPT(String filePath) {
        // create UI object for user interaction
        ui = new Ui();
        // create storage object which reads/writes data storage file
        storage = new Storage(filePath);
        // temporary variable for task list
        TaskList loadedTaskList;
        try {
            // try loading tasks from data storage file
            ArrayList<Task> fromDisk = storage.load();
            loadedTaskList = new TaskList(fromDisk);
        } catch (IOException e) {
            // if it cannot load throw error and make new task list
            ui.showLoadingError();
            loadedTaskList = new TaskList();
        }
        // assign tasks to proper task list
        tasks = loadedTaskList;
    }

    // start the application
    public void run() {
        // print greeting template
        ui.showGreeting();
        // if command is not bye read user input
        while (true) {
            // reads line of user input
            String input = ui.readCommand();
            // try parsing user input
            try {
                Parser.ParsedCommand command = Parser.parse(input);
                // if command is bye end application
                if (command.type == Parser.CommandType.BYE) {
                    ui.showBye();
                    ui.close();
                    return;
                }
                // execute the given command
                execute(command);
            // if there is an error show error message
            } catch (NUSGPTException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    // execute user command
    private void execute(Parser.ParsedCommand cmd) throws NUSGPTException {
        // try the given command
        try {
            switch (cmd.type) {
                case LIST:
                    ui.showList(tasks);
                    break;
                case MARK: {
                    Task t = getTaskByIndex(cmd.index);
                    t.markDone();
                    storage.save(tasks.taskArrayList());
                    ui.showTaskMarked(t);
                    break;
                }
                case UNMARK: {
                    Task t = getTaskByIndex(cmd.index);
                    t.markNotDone();
                    storage.save(tasks.taskArrayList());
                    ui.showTaskUnmarked(t);
                    break;
                }
                case DELETE: {
                    Task removed = removeTaskByUserIndex(cmd.index);
                    storage.save(tasks.taskArrayList());
                    ui.showTaskRemoved(removed, tasks.size());
                    break;
                }
                case TODO: {
                    checkTaskListCapacity();
                    Task task = new ToDo(cmd.description);
                    tasks.add(task);
                    storage.save(tasks.taskArrayList());
                    ui.showTaskAdded(task, tasks.size());
                    break;
                }
                case DEADLINE: {
                    checkTaskListCapacity();
                    Task task = new Deadline(cmd.description, cmd.date);
                    tasks.add(task);
                    storage.save(tasks.taskArrayList());
                    ui.showTaskAdded(task, tasks.size());
                    break;
                }
                case EVENT: {
                    checkTaskListCapacity();
                    Task task = new Event(cmd.description, cmd.start, cmd.end);
                    tasks.add(task);
                    storage.save(tasks.taskArrayList());
                    ui.showTaskAdded(task, tasks.size());
                    break;
                }
                // if command cannot be identified throw error
                default:
                    throw new NUSGPTException("unidentified instruction.\n");
            }
        // if there is an error saving data show error message
        } catch (IOException e) {
            ui.showSaveError();
        }
    }

    // check if the task list has space for new tasks
    private void checkTaskListCapacity() throws NUSGPTException {
        if (tasks.size() >= MAX_TASKS) {
            throw new NUSGPTException("no space for new tasks in task list.\n");
        }
    }

    // get task from task list with the given index
    private Task getTaskByIndex(int userIndex) throws NUSGPTException {
        if (userIndex < 1 || userIndex > tasks.size()) {
            throw new NUSGPTException(userIndex + " is not a valid index\n");
        }
        return tasks.get(userIndex - 1);
    }

    // remove task from task list with the given index
    private Task removeTaskByUserIndex(int userIndex) throws NUSGPTException {
        if (userIndex < 1 || userIndex > tasks.size()) {
            throw new NUSGPTException(userIndex + " is not a valid index\n");
        }
        return tasks.remove(userIndex - 1);
    }

    public static void main(String[] args) {
        new NUSGPT("data/NUSGPT.txt").run();
    }
}
