package nusgpt;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DateTimeTest {

    @Test
    void parseYearMonthDayDateInput() {
        DateTime.ParsedDateTime parsed = DateTime.parseUserInput("2026-01-31");
        assertNotNull(parsed);
        assertFalse(parsed.hasTime());
        LocalDateTime dt = parsed.getDateTime();
        assertEquals(LocalDateTime.of(2026, 1, 31, 0, 0), dt);
    }

    @Test
    void parseYearMonthDayDateAndTimeInput() {
        DateTime.ParsedDateTime parsed = DateTime.parseUserInput("2026-01-31 2359");
        assertNotNull(parsed);
        assertTrue(parsed.hasTime());
        assertEquals(LocalDateTime.of(2026, 1, 31, 23, 59), parsed.getDateTime());
    }

    @Test
    void parseDayMonthYearDateInput() {
        DateTime.ParsedDateTime parsed = DateTime.parseUserInput("31/1/2026");
        assertNotNull(parsed);
        assertFalse(parsed.hasTime());
        assertEquals(LocalDateTime.of(2026, 1, 31, 0, 0), parsed.getDateTime());
    }

    @Test
    void parseDayMonthYearDateAndTimeInput() {
        DateTime.ParsedDateTime parsed = DateTime.parseUserInput("31/1/2026 2359");
        assertNotNull(parsed);
        assertTrue(parsed.hasTime());
        assertEquals(LocalDateTime.of(2026, 1, 31, 23, 59), parsed.getDateTime());
    }

    @Test
    void parseInvalidDate() {
        // 2025-02-29 is invalid as it is not a leap year
        assertThrows(IllegalArgumentException.class, () -> DateTime.parseUserInput("2025-02-29"));
    }

    @Test
    void parseInvalidTime() {
        assertThrows(IllegalArgumentException.class, () -> DateTime.parseUserInput("2026-01-31 2400"));
        assertThrows(IllegalArgumentException.class, () -> DateTime.parseUserInput("31/1/2026 9999"));
    }

    @Test
    void nullEmpty() {
        assertFalse(DateTime.matchDateFormat(null));
        assertFalse(DateTime.matchDateFormat(""));
    }

    @Test
    void dateTextFormat() {
        assertFalse(DateTime.matchDateFormat("monday"));
        assertFalse(DateTime.matchDateFormat("20260131"));
    }
}
