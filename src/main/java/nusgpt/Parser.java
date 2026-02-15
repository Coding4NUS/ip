package nusgpt;

public class Parser {

    /**
     * All the supported command types
     */
    public enum CommandType {
        LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND, BYE
    }

    /**
     * Represents the data of the parsed command
     */
    public static class ParsedCommand {
        public final CommandType type;
        public final int index;
        public final String description;
        public final String date;
        public final String start;
        public final String end;

        /**
         * Constructor for ParsedCommand class
         *
         * @param type type of command
         * @param index index of task
         * @param description description of task
         * @param date date of task
         * @param start start date of task
         * @param end end date of task
         */
        private ParsedCommand(CommandType type, int index, String description, String date, String start, String end) {
            this.type = type;
            this.index = index;
            this.description = description;
            this.date = date;
            this.start = start;
            this.end = end;
        }

        /**
         * Represents simple commands with no fields (list/bye)
         *
         * @return ParsedCommand parsed command of simple type
         */
        public static ParsedCommand simple(CommandType type) {
            return new ParsedCommand(type, -1, null, null, null, null);
        }

        /**
         * Represents commands with an index (mark/unmark/delete)
         *
         * @return ParsedCommand parsed command with index
         */
        public static ParsedCommand withIndex(CommandType type, int index) {
            return new ParsedCommand(type, index, null, null, null, null);
        }

        /**
         * Represents todo command
         *
         * @param description description of todo task
         * @return ParsedCommand todo parsed command
         */
        public static ParsedCommand todo(String description) {
            return new ParsedCommand(CommandType.TODO, -1, description, null, null, null);
        }

        /**
         * Represents deadline command
         *
         * @param description description of deadline task
         * @param date date of deadline task
         * @return ParsedCommand deadline parsed command
         */
        public static ParsedCommand deadline(String description, String date) {
            return new ParsedCommand(CommandType.DEADLINE, -1, description, date, null, null);
        }

        /**
         * Represents event command
         *
         * @param description description of event task
         * @param start start date of event task
         * @param end end date of event task
         * @return ParsedCommand event parsed command
         */
        public static ParsedCommand event(String description, String start, String end) {
            return new ParsedCommand(CommandType.EVENT, -1, description, null, start, end);
        }

        /**
         * Find a task by searching for a keyword in the task description
         *
         * @param keyword word in task description you are finding
         * @return ParsedCommand command to find the task
         */
        public static ParsedCommand find(String keyword) {
            return new ParsedCommand(CommandType.FIND, -1, keyword, null, null, null);
        }
    }

    private static ParsedCommand parseFind(String command) throws NUSGPTException {
        final String keyword = command.length() > 4 ? command.substring(4).trim() : "";
        if (keyword.isEmpty()) {
            throw new NUSGPTException("please provide a keyword to find.\n");
        }
        return ParsedCommand.find(keyword);
    }

    private static ParsedCommand parseTodo(String command) throws NUSGPTException {
        String description = command.length() >= 5 ? command.substring(5).trim() : "";
        if (description.isEmpty()) {
            throw new NUSGPTException("please provide a description for the todo task.\n");
        }
        return ParsedCommand.todo(description);
    }

    private static ParsedCommand parseDeadline(String command) throws NUSGPTException {
        int dateIndex = command.indexOf(" /by ");
        if (dateIndex == -1) {
            throw new NUSGPTException("use the format: deadline (description) /by (date)\n");
        }

        String description = command.substring(8, dateIndex).trim();
        String date = command.substring(dateIndex + 5).trim();

        if (description.isEmpty()) {
            throw new NUSGPTException("please provide a description for the deadline task.\n");
        }
        if (date.isEmpty()) {
            throw new NUSGPTException("please provide a date for the deadline task.\n");
        }

        checkDateFormat(date);
        return ParsedCommand.deadline(description, date);
    }

    private static ParsedCommand parseEvent(String command) throws NUSGPTException {
        String taskInfo = command.length() >= 6 ? command.substring(6) : "";
        int startIndex = taskInfo.indexOf(" /from ");
        int endIndex = taskInfo.indexOf(" /to ");

        if (startIndex == -1 || endIndex == -1 || endIndex < startIndex) {
            throw new NUSGPTException("use the format: event (description) /from (start) /to (end)\n");
        }

        String description = taskInfo.substring(0, startIndex).trim();
        String start = taskInfo.substring(startIndex + 7, endIndex).trim();
        String end = taskInfo.substring(endIndex + 5).trim();

        if (description.isEmpty()) {
            throw new NUSGPTException("please provide a description for the event task.\n");
        }
        if (start.isEmpty()) {
            throw new NUSGPTException("please provide a time for the start of the event task.\n");
        }
        if (end.isEmpty()) {
            throw new NUSGPTException("please provide a time for the end of the event task.\n");
        }

        checkDateFormat(start);
        checkDateFormat(end);

        return ParsedCommand.event(description, start, end);
    }

    /**
     * Parses user input and assigns command to it
     *
     * @param input user input
     * @return ParsedCommand parsed command from user input
     * @throws NUSGPTException if user input is not a valid command
     */
    public static ParsedCommand parse(String input) throws NUSGPTException {
        if (input == null) {
            throw new NUSGPTException("command cannot be null\n");
        }

        String command = input.trim();
        if (command.isEmpty()) {
            throw new NUSGPTException("command cannot be empty\n");
        }

        if (command.equals("bye")) {
            return ParsedCommand.simple(CommandType.BYE);
        }
        if (command.equals("list")) {
            return ParsedCommand.simple(CommandType.LIST);
        }
        if (command.startsWith("find")) {
            return parseFind(command);
        }
        if (command.startsWith("mark")) {
            return ParsedCommand.withIndex(CommandType.MARK, parseIndex(command, "mark", 5));
        }
        if (command.startsWith("unmark")) {
            return ParsedCommand.withIndex(CommandType.UNMARK, parseIndex(command, "unmark", 7));
        }
        if (command.startsWith("delete")) {
            return ParsedCommand.withIndex(CommandType.DELETE, parseIndex(command, "delete", 7));
        }
        if (command.startsWith("todo")) {
            return parseTodo(command);
        }
        if (command.startsWith("deadline")) {
            return parseDeadline(command);
        }
        if (command.startsWith("event")) {
            return parseEvent(command);
        }

        throw new NUSGPTException("unidentified instruction. the following tasks are valid: todo, event, deadline, find\n");
    }

    /**
     * Parses index given after command
     *
     * @param command user input
     * @param keyword command keyword in input
     * @param prefixLength length of command
     * @return int index
     * @throws NUSGPTException if index is not the correct format
     */
    private static int parseIndex(String command, String keyword, int prefixLength) throws NUSGPTException {
        // get index text from command
        String index;
        if (command.length() > prefixLength) {
            index = command.substring(prefixLength).trim();
        } else {
            index = "";
        }
        // if index text is empty throw error
        if (index.isEmpty()) {
            throw new NUSGPTException("please provide an index for " + keyword + ".\n");
        }
        try {
            // parse the index
            return Integer.parseInt(index);
        // if index text is the wrong format empty throw error
        } catch (NumberFormatException e) {
            throw new NUSGPTException("invalid index provided for " + keyword + ".\n");
        }
    }

    /**
     * Checks if the date text matches the valid formats
     *
     * @param raw date text
     * @throws NUSGPTException if parsing fails
     */
    private static void checkDateFormat(String raw) throws NUSGPTException {
        if (DateTime.matchDateFormat(raw)) {
            try {
                DateTime.parseUserInput(raw);
            } catch (IllegalArgumentException ex) {
                throw new NUSGPTException(ex.getMessage() + "\n");
            }
        }
    }
}
