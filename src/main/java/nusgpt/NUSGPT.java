package nusgpt;

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

    /**
     * Runs the application
     */
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

    /**
     * Matches user input with command and executes it
     *
     * @param cmd Parsed user command
     * @throws NUSGPTException If command cannot be identified
     */
    private void execute(Parser.ParsedCommand cmd) throws NUSGPTException {
        // try the given command
        try {
            switch (cmd.type) {
                case LIST:
                    ui.showList(tasks);
                    break;
                case FIND:
                    ui.showFindResults(tasks.findByKeyword(cmd.description));
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

    private String executeForGui(Parser.ParsedCommand cmd) {
        try {
            switch (cmd.type) {
                case LIST:
                    return ui.formatList(tasks);

                case FIND:
                    return ui.formatFindResults(tasks.findByKeyword(cmd.description));

                case MARK: {
                    Task t = getTaskByIndex(cmd.index);
                    t.markDone();
                    storage.save(tasks.taskArrayList());
                    return ui.formatTaskMarked(t);
                }

                case UNMARK: {
                    Task t = getTaskByIndex(cmd.index);
                    t.markNotDone();
                    storage.save(tasks.taskArrayList());
                    return ui.formatTaskUnmarked(t);
                }

                case DELETE: {
                    Task removed = removeTaskByUserIndex(cmd.index);
                    storage.save(tasks.taskArrayList());
                    return ui.formatTaskRemoved(removed, tasks.size());
                }

                case TODO: {
                    checkTaskListCapacity();
                    Task task = new ToDo(cmd.description);
                    tasks.add(task);
                    storage.save(tasks.taskArrayList());
                    return ui.formatTaskAdded(task, tasks.size());
                }

                case DEADLINE: {
                    checkTaskListCapacity();
                    Task task = new Deadline(cmd.description, cmd.date);
                    tasks.add(task);
                    storage.save(tasks.taskArrayList());
                    return ui.formatTaskAdded(task, tasks.size());
                }

                case EVENT: {
                    checkTaskListCapacity();
                    Task task = new Event(cmd.description, cmd.start, cmd.end);
                    tasks.add(task);
                    storage.save(tasks.taskArrayList());
                    return ui.formatTaskAdded(task, tasks.size());
                }

                default:
                    return ui.formatError("unidentified instruction.\n");
            }
        } catch (IOException e) {
            return ui.formatSaveError();
        } catch (NUSGPTException e) {
            return ui.formatError(e.getMessage());
        }
    }

    /**
     * Check if the task list has space for new tasks
     *
     * @throws NUSGPTException If there is no space in the task list
     */
    private void checkTaskListCapacity() throws NUSGPTException {
        if (tasks.size() >= MAX_TASKS) {
            throw new NUSGPTException("no space for new tasks in task list.\n");
        }
    }

    /**
     * Get task from task list with given index
     *
     * @param index index of task
     * @return Task task
     * @throws NUSGPTException If index does not match any task in task list
     */
    private Task getTaskByIndex(int index) throws NUSGPTException {
        if (index < 1 || index > tasks.size()) {
            throw new NUSGPTException(index + " is not a valid index\n");
        }
        return tasks.get(index - 1);
    }

    /**
     * Remove task from task list with given index
     *
     * @param index index of task
     * @return Task task
     * @throws NUSGPTException If index does not match any task in task list
     */
    private Task removeTaskByUserIndex(int index) throws NUSGPTException {
        if (index < 1 || index > tasks.size()) {
            throw new NUSGPTException(index + " is not a valid index\n");
        }
        return tasks.remove(index - 1);
    }

    public static void main(String[] args) {
        new NUSGPT("data/nusgpt.NUSGPT.txt").run();
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            Parser.ParsedCommand command = Parser.parse(input);

            if (command.type == Parser.CommandType.BYE) {
                return ui.formatBye();
            }

            return executeForGui(command);

        } catch (NUSGPTException e) {
            return ui.formatError(e.getMessage());
        }
    }

    public String getGreeting() {
        return ui.formatGreeting();
    }

    public boolean isExitCommand(String input) {
        try {
            return Parser.parse(input).type == Parser.CommandType.BYE;
        } catch (NUSGPTException e) {
            return false;
        }
    }
}
