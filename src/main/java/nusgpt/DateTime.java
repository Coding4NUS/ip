package nusgpt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DateTime {
    /**
     * Wrapper representing a parsed date/time value
     */
    public static class ParsedDateTime {
        // stores the parsed date/time value
        private final LocalDateTime dateTime;
        // if the date has a time
        private final boolean hasTime;

        // class constructor
        public ParsedDateTime(LocalDateTime dateTime, boolean hasTime) {
            this.dateTime = dateTime;
            this.hasTime = hasTime;
        }

        // returns date/time
        public LocalDateTime getDateTime() {
            return dateTime;
        }

        // returns if the date has a time
        public boolean hasTime() {
            return hasTime;
        }
    }

    // date/time formats with yyyy MM dd
    private static final DateTimeFormatter INPUT_YMD =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter INPUT_YMD_TIME =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm").withResolverStyle(ResolverStyle.STRICT);

    // date/time formats with dd MM yyyy
    private static final DateTimeFormatter INPUT_DMY =
            DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter INPUT_DMY_TIME =
            DateTimeFormatter.ofPattern("d/M/uuuu HHmm").withResolverStyle(ResolverStyle.STRICT);

    // date/time format for data storage
    private static final DateTimeFormatter STORAGE_DATE =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter STORAGE_DATETIME =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm").withResolverStyle(ResolverStyle.STRICT);

    // date/time format for print
    private static final DateTimeFormatter PRINT_DATE =
            DateTimeFormatter.ofPattern("MMM dd uuuu");
    private static final DateTimeFormatter PRINT_DATETIME =
            DateTimeFormatter.ofPattern("MMM dd uuuu HH:mm");

    /**
     * Check if user input string matches date format
     *
     * @param raw user input string
     * @return true if string follows the date format / false if it does not
     */
    public static boolean matchDateFormat(String raw) {
        // if the raw string is null or empty return false
        if (raw == null) {
            return false;
        }
        String string = raw.trim();
        if (string.isEmpty()) {
            return false;
        }
        // check if raw string has separator symbols and digits
        return (string.contains("-") || string.contains("/")) && string.chars().anyMatch(Character::isDigit);
    }

    /**
     * Parses user input in relevant format
     *
     * @param raw user input string
     * @return ParsedDateTime
     * @throws IllegalArgumentException If invalid date/time format
     */
    public static ParsedDateTime parseUserInput(String raw) {
        String string = raw.trim();

        // try date/time format
        ParsedDateTime dt = ParseDateTime(string);
        if (dt != null) {
            return dt;
        }

        // try date format
        LocalDate date = ParseDate(string);
        if (date != null) {
            return new ParsedDateTime(LocalDateTime.of(date, LocalTime.MIDNIGHT), false);
        }

        throw new IllegalArgumentException("invalid date/time format");
    }

    // date format for print
    public static String datePrintFormat(LocalDateTime dt, boolean hasTime) {
        return hasTime ? dt.format(PRINT_DATETIME) : dt.toLocalDate().format(PRINT_DATE);
    }

    /**
     * Parses date text by comparing it to formats
     *
     * @param dt String of date text
     * @return String
     * @throws DateTimeParseException If no match.
     */
    public static String dateStorageFormat(LocalDateTime dt, boolean hasTime) {
        return hasTime ? dt.format(STORAGE_DATETIME) : dt.toLocalDate().format(STORAGE_DATE);
    }

    private static ParsedDateTime ParseDateTime(String s) {
        // yyyy-MM-dd HHmm
        try {
            return new ParsedDateTime(LocalDateTime.parse(s, INPUT_YMD_TIME), true);
        } catch (DateTimeParseException ignored) { }

        // d/M/yyyy HHmm
        try {
            return new ParsedDateTime(LocalDateTime.parse(s, INPUT_DMY_TIME), true);
        } catch (DateTimeParseException ignored) { }

        return null;
    }

    /**
     * Parses date text by comparing it to formats
     * If it does not match throw exception
     *
     * @param s String of date text
     * @return LocalDate
     * @throws DateTimeParseException If no match.
     */
    private static LocalDate ParseDate(String s) {
        // yyyy-MM-dd
        try {
            return LocalDate.parse(s, INPUT_YMD);
        } catch (DateTimeParseException ignored) { }

        // d/M/yyyy
        try {
            return LocalDate.parse(s, INPUT_DMY);
        } catch (DateTimeParseException ignored) { }

        return null;
    }
}

