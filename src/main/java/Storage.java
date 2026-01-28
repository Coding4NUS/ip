import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    // hard-coded relative file path
    private static final String FILE_PATH = "./data/NUSGPT.txt";
    // symbol that separates data types
    private static final String SEPARATOR = " | ";

    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(FILE_PATH);

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
                // convert the text to a task and add it to the task list
                try {
                    Task t = parseLine(line);
                    if (t != null) {
                        tasks.add(t);
                    }
                } catch (RuntimeException ex) {
                    // ignore runtime error
                }
            }
        }
        // return the list of tasks loaded from the file
        return tasks;
    }

    public void save(ArrayList<Task> tasks) throws IOException {
        // ensures data file exists
        ensureDataFileExists();

        // opens filewriter which overwrites the file each time
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            // for each task in the task list
            for (Task t : tasks) {
                // write the task in the data storage format
                writer.write(t.toFileString());
                // separate the line
                writer.write(System.lineSeparator());
            }
        }
    }

    // create data file if it is missing
    private void ensureDataFileExists() throws IOException {
        // reference to the ./data path
        File directory = new File("./data");
        // check if ./data path currently exists
        if (!directory.exists()) {
            // if mkdirs cannot make the directory throw exception
            if (!directory.mkdirs()) {
                throw new IOException("could not create data directory: " + directory.getPath());
            }
        }
        // reference to the data storage path
        File file = new File(FILE_PATH);
        // check if data storage file currently exists
        if (!file.exists()) {
            // if the data storage file cannot be created throw exception
            if (!file.createNewFile()) {
                throw new IOException("could not create data file: " + file.getPath());
            }
        }
    }

    // converts line of text to a task
    private Task parseLine(String line) {
        // splits the line by the separator
        String[] parts = line.split("\\Q" + SEPARATOR + "\\E", -1);
        // task type
        String type = parts[0].trim();
        // if task is done or not
        int done = Integer.parseInt(parts[1].trim());
        // task description
        String description = parts[2];
        // new task which stores data
        Task task;

        switch (type) {
            // create todo task
            case "T":
                if (parts.length != 3) {
                    return null;
                }
                task = new ToDo(description);
                break;
            // create deadline task
            case "D":
                if (parts.length != 4) {
                    return null;
                }
                String by = parts[3].trim();
                task = new Deadline(description, by);
                break;
            // create event task
            case "E":
                if (parts.length != 5) {
                    return null;
                }
                String from = parts[3];
                String to = parts[4];
                // if there is no from or to return null
                if (from.isEmpty() || to.isEmpty()) {
                    return null;
                }
                task = new Event(description, from, to);
                break;
            // if none of the task types are recognised throw error
            default:
                throw new IllegalArgumentException("unknown task type: " + type);
        }
        // mark the task as done if it is 1 in the data file
        if (done == 1) {
            task.markDone();
        }
        return task;
    }
}