package nusgpt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Utility class for parsing, formatting, and validating user-provided date/time strings.
 */
// generated JavaDoc comment using ChatGPT
public class DateTime {

    /**
     * Encapsulates a parsed LocalDateTime and whether a time component was provided.
     */
    // generated JavaDoc comment using ChatGPT
    public static class ParsedDateTime {
        // stores the parsed date/time value
        private final LocalDateTime dateTime;
        // if the date has a time
        private final boolean hasTime;

        /**
         * Creates a parsed date-time wrapper.
         *
         * @param dateTime Parsed date-time value (non-null).
         * @param hasTime  True if the user provided a time component.
         */
        // generated JavaDoc comment using ChatGPT
        public ParsedDateTime(LocalDateTime dateTime, boolean hasTime) {
            this.dateTime = dateTime;
            this.hasTime = hasTime;
        }

        /**
         * Returns the parsed date-time.
         *
         * @return Parsed LocalDateTime.
         */
        // generated JavaDoc comment using ChatGPT
        public LocalDateTime getDateTime() {
            return dateTime;
        }

        /**
         * Returns whether the original input contained a time component.
         *
         * @return True if time was provided; false otherwise.
         */
        // generated JavaDoc comment using ChatGPT
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
     * Performs a lightweight check to see if the input resembles a date/time by checking for
     * separators ('-' or '/') and at least one digit.
     *
     * <p>This method does not guarantee the string is parseable; it is only a cheap pre-check.</p>
     *
     * @param raw Raw input string.
     * @return True if the string resembles a date/time; false otherwise.
     */
    // generated JavaDoc comment using ChatGPT
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
     * Parses user input into a date-time (time defaults to midnight when not present).
     *
     * @param raw Raw input string (non-null).
     * @return Parsed date-time wrapper.
     * @throws IllegalArgumentException If input cannot be parsed as a supported date/time format.
     */
    // generated JavaDoc comment using ChatGPT
    public static ParsedDateTime parseUserInput(String raw) {
        assert raw != null : "user input should not be null";
        String string = raw.trim();

        // try date/time format
        ParsedDateTime dt = parseDateTime(string);
        if (dt != null) {
            return dt;
        }

        // try date format
        LocalDate date = parseDate(string);
        if (date != null) {
            return new ParsedDateTime(LocalDateTime.of(date, LocalTime.MIDNIGHT), false);
        }

        throw new IllegalArgumentException("invalid date/time format");
    }

    /**
     * Formats a parsed date-time for display to the user.
     *
     * @param dt Parsed date-time value.
     * @param hasTime  True if time should be displayed.
     * @return Formatted display string.
     */
    // generated JavaDoc comment using ChatGPT
    public static String datePrintFormat(LocalDateTime dt, boolean hasTime) {
        return hasTime ? dt.format(PRINT_DATETIME) : dt.toLocalDate().format(PRINT_DATE);
    }

    /**
     * Formats a parsed date-time for storage to disk.
     *
     * @param dt Parsed date-time value.
     * @param hasTime  True if time should be stored.
     * @return Storage-formatted string.
     */
    // generated JavaDoc comment using ChatGPT
    public static String dateStorageFormat(LocalDateTime dt, boolean hasTime) {
        return hasTime ? dt.format(STORAGE_DATETIME) : dt.toLocalDate().format(STORAGE_DATE);
    }

    /**
     * Tries to parse user input; returns null instead of throwing when invalid.
     *
     * @param raw Raw input string.
     * @return Parsed date-time wrapper, or null if invalid/blank.
     */
    // generated JavaDoc comment using ChatGPT
    public static ParsedDateTime tryParseUserInput(String raw) {
        if (raw == null) {
            return null;
        }
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        try {
            return parseUserInput(trimmed);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * Attempts parsing date + time formats.
     *
     * @param s Trimmed input (non-null).
     * @return Parsed wrapper if successful; null otherwise.
     */
    // generated JavaDoc comment using ChatGPT
    private static ParsedDateTime parseDateTime(String s) {
        assert s != null : "string should not be null";
        // yyyy-MM-dd HHmm
        try {
            return new ParsedDateTime(LocalDateTime.parse(s, INPUT_YMD_TIME), true);
        } catch (DateTimeParseException ignored) {
            // ignore to try other formats
        }

        // d/M/yyyy HHmm
        try {
            return new ParsedDateTime(LocalDateTime.parse(s, INPUT_DMY_TIME), true);
        } catch (DateTimeParseException ignored) {
            // ignore to try date-only formats
        }
        return null;
    }

    /**
     * Attempts parsing date-only formats.
     *
     * @param s Trimmed input (non-null).
     * @return Parsed LocalDate if successful; null otherwise.
     */
    // generated JavaDoc comment using ChatGPT
    private static LocalDate parseDate(String s) {
        assert s != null : "string should not be null";
        // yyyy-MM-dd
        try {
            return LocalDate.parse(s, INPUT_YMD);
        } catch (DateTimeParseException ignored) {
            // ignore to try other formats
        }

        // d/M/yyyy
        try {
            return LocalDate.parse(s, INPUT_DMY);
        } catch (DateTimeParseException ignored) {
            // if all parsing fails caller will treat as invalid
        }
        return null;
    }
}