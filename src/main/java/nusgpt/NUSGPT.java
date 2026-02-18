package nusgpt;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Main application logic for NUSGPT.
 * Manages parsing, task list operations, persistence, and formatting via Ui.
 */
// generated JavaDoc comment using ChatGPT
public class NUSGPT {
    // store up to 100 tasks
    private static final int MAX_TASKS = 100;
    // stores and loads tasks from the disk
    private final Storage storage;
    // list of tasks
    private final TaskList tasks;
    // templates for user interaction
    private final Ui ui;

    /**
     * Constructs NUSGPT and loads tasks from storage.
     *
     * @param filePath Path to the storage file.
     */
    // generated JavaDoc comment using ChatGPT
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
            } catch (NusGptException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Matches user input with command and executes it
     *
     * @param cmd Parsed user command
     * @throws NusGptException If command cannot be identified
     */
    private void execute(Parser.ParsedCommand cmd) throws NusGptException {
        // try the given command
        try {
            switch (cmd.type) {
            case LIST:
                ui.showList(tasks);
                break;
            case FIND:
                ui.showFindResults(tasks.findByKeyword(cmd.description));
                break;
            case HELP:
                ui.showHelp();
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
                throw new NusGptException("unidentified instruction.\n");
            }
        // if there is an error saving data show error message
        } catch (IOException e) {
            ui.showSaveError();
        }
    }

    /**
     * Executes a parsed command for GUI mode (returns formatted response).
     *
     * @param cmd Parsed user command.
     * @return Response string for GUI.
     */
    // generated JavaDoc comment using ChatGPT
    private String executeForGui(Parser.ParsedCommand cmd) {
        try {
            switch (cmd.type) {
            case LIST:
                return ui.formatList(tasks);

            case FIND:
                return ui.formatFindResults(tasks.findByKeyword(cmd.description));

            case HELP:
                return ui.formatHelp();

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
        } catch (NusGptException e) {
            return ui.formatError(e.getMessage());
        }
    }

    /**
     * Check if the task list has space for new tasks
     *
     * @throws NusGptException If there is no space in the task list
     */
    private void checkTaskListCapacity() throws NusGptException {
        if (tasks.size() >= MAX_TASKS) {
            throw new NusGptException("no space for new tasks in task list.\n");
        }
    }

    /**
     * Get task from task list with given index
     *
     * @param index index of task
     * @return Task task
     * @throws NusGptException If index does not match any task in task list
     */
    private Task getTaskByIndex(int index) throws NusGptException {
        if (index < 1 || index > tasks.size()) {
            throw new NusGptException(index + " is not a valid index\n");
        }
        return tasks.get(index - 1);
    }

    /**
     * Remove task from task list with given index
     *
     * @param index index of task
     * @return Task task
     * @throws NusGptException If index does not match any task in task list
     */
    private Task removeTaskByUserIndex(int index) throws NusGptException {
        if (index < 1 || index > tasks.size()) {
            throw new NusGptException(index + " is not a valid index\n");
        }
        return tasks.remove(index - 1);
    }

    /**
     * Console entry point (kept for completeness).
     *
     * @param args CLI arguments.
     */
    // generated JavaDoc comment using ChatGPT
    public static void main(String[] args) {
        new NUSGPT("data/nusgpt.NUSGPT.txt").run();
    }

    /**
     * Generates a response for the user's chat message.
     * @param input User input.
     * @return Response string.
     */
    public String getResponse(String input) {
        try {
            Parser.ParsedCommand command = Parser.parse(input);

            if (command.type == Parser.CommandType.BYE) {
                return ui.formatBye();
            }

            return executeForGui(command);

        } catch (NusGptException e) {
            return ui.formatError(e.getMessage());
        }
    }

    /**
     * Returns greeting string for GUI.
     *
     * @return Greeting.
     */
    // generated JavaDoc comment using ChatGPT
    public String getGreeting() {
        return ui.formatGreeting();
    }

    /**
     * Checks if the given input is an exit command.
     *
     * @param input Raw user input.
     * @return True if input parses to BYE; false otherwise.
     */
    // generated JavaDoc comment using ChatGPT
    public boolean isExitCommand(String input) {
        try {
            return Parser.parse(input).type == Parser.CommandType.BYE;
        } catch (NusGptException e) {
            return false;
        }
    }
}
