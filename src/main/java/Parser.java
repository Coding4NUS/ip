public class Parser {

    // all the supported command types
    public enum CommandType {
        LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, BYE
    }

    // represents the data of parsed command
    public static class ParsedCommand {
        public final CommandType type;
        public final int index;
        public final String description;
        public final String date;
        public final String start;
        public final String end;

        // constructor for ParsedCommand class
        private ParsedCommand(CommandType type, int index, String description, String date, String start, String end) {
            this.type = type;
            this.index = index;
            this.description = description;
            this.date = date;
            this.start = start;
            this.end = end;
        }

        // commands with no fields (list/bye)
        public static ParsedCommand simple(CommandType type) {
            return new ParsedCommand(type, -1, null, null, null, null);
        }

        // commands with an index (mark/unmark/delete)
        public static ParsedCommand withIndex(CommandType type, int index) {
            return new ParsedCommand(type, index, null, null, null, null);
        }

        // todo command
        public static ParsedCommand todo(String description) {
            return new ParsedCommand(CommandType.TODO, -1, description, null, null, null);
        }

        // deadline command
        public static ParsedCommand deadline(String description, String by) {
            return new ParsedCommand(CommandType.DEADLINE, -1, description, by, null, null);
        }

        // event command
        public static ParsedCommand event(String description, String from, String to) {
            return new ParsedCommand(CommandType.EVENT, -1, description, null, from, to);
        }
    }

    // parse command
    public static ParsedCommand parse(String input) throws NUSGPTException {
        // if command is null throw error
        if (input == null) {
            throw new NUSGPTException("command cannot be null\n");
        }
        // if command is empty throw error
        String command = input.trim();
        if (command.isEmpty()) {
            throw new NUSGPTException("command cannot be empty\n");
        }
        // if input is "bye" return bye command
        if (command.equals("bye")) {
            return ParsedCommand.simple(CommandType.BYE);
        }
        // if input is "list" return list command
        if (command.equals("list")) {
            return ParsedCommand.simple(CommandType.LIST);
        }
        // if input is "mark" parse index then return mark command
        if (command.startsWith("mark")) {
            int index = parseIndex(command, "mark", 5);
            return ParsedCommand.withIndex(CommandType.MARK, index);
        }
        // if input is "unmark" parse index then return unmark command
        if (command.startsWith("unmark")) {
            int index = parseIndex(command, "unmark", 7);
            return ParsedCommand.withIndex(CommandType.UNMARK, index);
        }
        // if input is "delete" parse index then return delete command
        if (command.startsWith("delete")) {
            int index = parseIndex(command, "delete", 7);
            return ParsedCommand.withIndex(CommandType.DELETE, index);
        }
        // parse todo task
        if (command.startsWith("todo")) {
            // get description of todo from input
            String description = command.length() >= 5 ? command.substring(5).trim() : "";
            // if description is empty throw error
            if (description.isEmpty()) {
                throw new NUSGPTException("please provide a description for the todo task.\n");
            }
            // return todo task
            return ParsedCommand.todo(description);
        }
        // parse deadline task
        if (command.startsWith("deadline")) {
            // the date of the deadline is after the " /by " text
            int dateIndex = command.indexOf(" /by ");
            // if there is no date index throw an error
            if (dateIndex == -1) {
                throw new NUSGPTException("use the format: deadline (description) /by (date)\n");
            }
            // get the description of the deadline task from the input
            String description = command.substring(8, dateIndex).trim();
            // get the date of the deadline task from the input
            String date = command.substring(dateIndex + 5).trim();
            // if the description is empty throw an error
            if (description.isEmpty()) {
                throw new NUSGPTException("please provide a description for the deadline task.\n");
            }
            // if the date is empty throw an error
            if (date.isEmpty()) {
                throw new NUSGPTException("please provide a date for the deadline task.\n");
            }
            // check if the text fits the date format
            checkDateFormat(date);
            // return deadline task
            return ParsedCommand.deadline(description, date);
        }
        // parse event task
        if (command.startsWith("event")) {
            // get information of the event task from the input
            String taskInfo = command.length() >= 6 ? command.substring(6) : "";
            // the start of the event is after the " /from " text
            int startIndex = taskInfo.indexOf(" /from ");
            // the end of the event is after the " /to " text
            int endIndex = taskInfo.indexOf(" /to ");
            // if there is no start index throw an error
            if (startIndex == -1) {
                throw new NUSGPTException("invalid value for 'from' input for event task\n");
            }
            // if there is no start index throw an error
            if (endIndex == -1) {
                throw new NUSGPTException("invalid value for 'to' input for event task\n");
            }
            // if the format is invalid throw an error
            if (endIndex < startIndex) {
                throw new NUSGPTException("use the format: event (description) /from (start) /to (end)\n");
            }
            // get the description of the event task from the input
            String description = taskInfo.substring(0, startIndex).trim();
            // get the start of the deadline task from the input
            String start = taskInfo.substring(startIndex + 7, endIndex).trim();
            // get the end of the deadline task from the input
            String end = taskInfo.substring(endIndex + 5).trim();
            // if the description is empty throw an error
            if (description.isEmpty()) {
                throw new NUSGPTException("please provide a description for the event task.\n");
            }
            // if the start time is empty throw an error
            if (start.isEmpty()) {
                throw new NUSGPTException("please provide a time for the start of the event task.\n");
            }
            // if the end time is empty throw an error
            if (end.isEmpty()) {
                throw new NUSGPTException("please provide a time for the end of the event task.\n");
            }
            // check if the start text fits the date format
            checkDateFormat(start);
            // check if the end text fits the date format
            checkDateFormat(end);
            // return event task
            return ParsedCommand.event(description, start, end);
        }
        // if command does not match any task throw error
        throw new NUSGPTException("unidentified instruction. the following tasks are valid: todo, event, deadline\n");
    }

    // parse indexes
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

    // check if date text follows date format
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
