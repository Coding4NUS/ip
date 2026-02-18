package nusgpt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Saves tasks to and loads tasks from storage file
 */
public class Storage {
    // path to data storage file
    private final String filePath;
    // symbol that separates data types
    private static final String SEPARATOR = " | ";

    /**
     * Constructor for storage class
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from storage file
     *
     * @return ArrayList<Task> array list of tasks from the storage file
     * @throws IOException if storage file cannot be read or created
     */
    public ArrayList<Task> load() throws IOException {
        // array list to store the tasks
        ArrayList<Task> tasks = new ArrayList<>();
        // represents storage file
        File file = new File(filePath);
        // ensures data file exists
        ensureDataFileExists();
        // use scanner to read the data file
        try (Scanner fileScanner = new Scanner(file)) {
            // check every line of text in the data file
            while (fileScanner.hasNextLine()) {
                // reads the next line and ignores spaces
                String line = fileScanner.nextLine().trim();
                // if the line is blank skip it
                if (line.isEmpty()) {
                    continue;
                }
                // try to parse text to task
                Task task = parseLine(line);
                if (task != null) {
                    // if it can parse add it to task list
                    tasks.add(task);
                }
            }
        }
        // return the list of tasks loaded from the file
        return tasks;
    }

    /**
     * Saves tasks to storage file
     *
     * @param tasks array list of tasks from the storage file
     * @throws IOException if cannot write to storage file
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        // ensures data file exists
        ensureDataFileExists();
        // opens file writer which overwrites the file each time
        try (FileWriter writer = new FileWriter(filePath)) {
            // for each task in the task list
            for (Task t : tasks) {
                // write the task in the data storage format
                writer.write(t.toFileString());
                // separate the line
                writer.write(System.lineSeparator());
            }
        }
    }

    /**
     * Checks if data file exists and creates one if it is missing
     *
     * @throws IOException if it cannot make a directory or file
     */
    private void ensureDataFileExists() throws IOException {
        // represents storage file
        File file = new File(filePath);
        // represents directory file
        File directory = file.getParentFile();
        // check if data path currently exists
        if (directory != null && !directory.exists()) {
            // if mkdirs cannot make the directory throw exception
            if (!directory.mkdirs()) {
                throw new IOException("could not create data directory: " + directory.getPath());
            }
        }
        // check if data storage file currently exists
        if (!file.exists()) {
            // if the data storage file cannot be created throw exception
            if (!file.createNewFile()) {
                throw new IOException("could not create data file: " + file.getPath());
            }
        }
    }

    /**
     * Converts line of user input to task
     *
     * @param line line of user input
     * @return Task task from user input
     */
    private Task parseLine(String line) {
        assert line != null : "line must not be null";
        // splits the line by the separator
        String[] parts = line.split("\\Q" + SEPARATOR + "\\E", -1);
        assert parts.length >= 3 : "storage line must have at least 3 parts";
        // task type
        String type = parts[0].trim();
        // if task is done or not
        int done = Integer.parseInt(parts[1].trim());
        assert done == 0 || done == 1 : "done flag must be 0 or 1";
        // task description
        String desc = parts[2];
        assert desc != null : "description field must not be null";
        // new task which stores data
        Task task;

        switch (type) {
        // create todo task
        case "T":
            if (parts.length != 3) {
                return null;
            }
            task = new ToDo(desc);
            break;
        // create deadline task
        case "D":
            if (parts.length != 4) {
                return null;
            }
            String byStored = parts[3].trim();
            task = new Deadline(desc, byStored);
            break;
        // create event task
        case "E":
            if (parts.length != 5) {
                return null;
            }
            String start = parts[3].trim();
            String end = parts[4].trim();
            task = new Event(desc, start, end);
            break;
        // if none of the task types are recognised throw error
        default:
            return null;
        }
        // mark the task as done if it is 1 in the data file
        if (done == 1) {
            task.markDone();
        }
        return task;
    }
}
