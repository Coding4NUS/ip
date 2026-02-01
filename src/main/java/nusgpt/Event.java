package nusgpt;

import java.time.LocalDateTime;

public class Event extends Task {
    private LocalDateTime startDateTime;
    private boolean startHasTime;
    private String startText;

    private LocalDateTime endDateTime;
    private boolean endHasTime;
    private String endText;

    public Event(String description, String startRaw, String endRaw) {
        super(description);
        setStart(startRaw);
        setEnd(endRaw);
    }

    /**
     * Parses and stores the start date for the event
     *
     * @param string start date text
     */
    private void setStart(String string) {
        String startDate;
        // check if date text is null or empty
        if (string == null) {
            startDate = "";
        } else {
            startDate = string.trim();
        }
        if (startDate.isEmpty()) {
            startText = "";
            startDateTime = null;
            startHasTime = false;
            return;
        }
        // check if date text does not fit date format
        if (!DateTime.matchDateFormat(startDate)) {
            startText = startDate;
            startDateTime = null;
            startHasTime = false;
            return;
        }
        // if date text fits format parse
        DateTime.ParsedDateTime parsed = DateTime.parseUserInput(startDate);
        startDateTime = parsed.getDateTime();
        startHasTime = parsed.hasTime();
        startText = null;
    }

    /**
     * Parses and stores the end date for the event
     *
     * @param string end date text
     */
    private void setEnd(String string) {
        String endDate;
        // check if date text is null or empty
        if (string == null) {
            endDate = "";
        } else {
            endDate = string.trim();
        }
        if (endDate.isEmpty()) {
            endText = "";
            endDateTime = null;
            endHasTime = false;
            return;
        }
        // check if date text does not fit date format
        if (!DateTime.matchDateFormat(endDate)) {
            endText = endDate;
            endDateTime = null;
            endHasTime = false;
            return;
        }
        // if date text fits format parse
        DateTime.ParsedDateTime parsed = DateTime.parseUserInput(endDate);
        endDateTime = parsed.getDateTime();
        endHasTime = parsed.hasTime();
        endText = null;
    }

    /**
     * Returns the start date for the event
     *
     * @return String text for the start date
     */
    public String getStart() {
        if (startDateTime != null) {
            return DateTime.datePrintFormat(startDateTime, startHasTime);
        }
        return startText;
    }

    /**
     * Returns the end date for the event
     *
     * @return String text for the end date
     */
    public String getEnd() {
        if (endDateTime != null) {
            return DateTime.datePrintFormat(endDateTime, endHasTime);
        }
        return endText;
    }

    /**
     * String format of event information
     *
     * @return String format for event
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + getStart() + " to: " + getEnd() + ")";
    }

    /**
     * Converts start and end dates to storage format
     *
     * @return String storage format for start and end dates
     */
    @Override
    public String toFileString() {
        // store date in storage format
        String startStored = (startDateTime != null)
                ? DateTime.dateStorageFormat(startDateTime, startHasTime)
                : startText;

        String endStored = (endDateTime != null)
                ? DateTime.dateStorageFormat(endDateTime, endHasTime)
                : endText;
        return "E | " + (isDone() ? "1" : "0") + " | " + getDescription()
                + " | " + startStored + " | " + endStored;
    }
}
