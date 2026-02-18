package nusgpt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    void parse_nullEmptyUnknownCommand() {
        assertThrows(NusGptException.class, () -> Parser.parse(null));
        assertThrows(NusGptException.class, () -> Parser.parse(""));
        assertThrows(NusGptException.class, () -> Parser.parse("unknown command"));
    }

    @Test
    void parse_simpleCommands() throws Exception {
        Parser.ParsedCommand bye = Parser.parse("bye");
        assertEquals(Parser.CommandType.BYE, bye.type);

        Parser.ParsedCommand list = Parser.parse("list");
        assertEquals(Parser.CommandType.LIST, list.type);
    }

    @Test
    void parse_indexCommands() throws Exception {
        Parser.ParsedCommand mark = Parser.parse("mark 1");
        assertEquals(Parser.CommandType.MARK, mark.type);
        assertEquals(1, mark.index);

        Parser.ParsedCommand unmark = Parser.parse("unmark 2");
        assertEquals(Parser.CommandType.UNMARK, unmark.type);
        assertEquals(2, unmark.index);

        Parser.ParsedCommand delete = Parser.parse("delete 3");
        assertEquals(Parser.CommandType.DELETE, delete.type);
        assertEquals(3, delete.index);

        // missing mark index
        assertThrows(NusGptException.class, () -> Parser.parse("mark"));

        // incorrect mark index
        assertThrows(NusGptException.class, () -> Parser.parse("mark abc"));

        // missing unmark index
        assertThrows(NusGptException.class, () -> Parser.parse("unmark"));

        // incorrect unmark index
        assertThrows(NusGptException.class, () -> Parser.parse("unmark abc"));

        // missing delete index
        assertThrows(NusGptException.class, () -> Parser.parse("delete"));

        // incorrect unmark index
        assertThrows(NusGptException.class, () -> Parser.parse("delete abc"));
    }

    @Test
    void parse_todoCommand() throws Exception {
        Parser.ParsedCommand todo = Parser.parse("todo read book");
        assertEquals(Parser.CommandType.TODO, todo.type);
        assertEquals("read book", todo.description);

        // missing description
        assertThrows(NusGptException.class, () -> Parser.parse("todo"));
    }

    @Test
    void parse_deadlineCommand() throws Exception {
        Parser.ParsedCommand dl1 = Parser.parse("deadline return book /by 2/12/2019 1800");
        assertEquals(Parser.CommandType.DEADLINE, dl1.type);
        assertEquals("return book", dl1.description);
        assertEquals("2/12/2019 1800", dl1.date);

        Parser.ParsedCommand dl2 = Parser.parse("deadline return book /by 2019-12-02");
        assertEquals(Parser.CommandType.DEADLINE, dl2.type);
        assertEquals("return book", dl2.description);
        assertEquals("2019-12-02", dl2.date);

        // missing description
        assertThrows(NusGptException.class, () -> Parser.parse("deadline /by 2/12/2019 1800"));

        // missing /by
        assertThrows(NusGptException.class, () -> Parser.parse("deadline return book 2/12/2019 1800"));

        // missing date
        assertThrows(NusGptException.class, () -> Parser.parse("deadline return book /by"));

        // invalid date
        assertThrows(NusGptException.class, () -> Parser.parse("deadline return book /by 50/30/2019"));
    }

    @Test
    void parse_eventCommand() throws Exception {
        Parser.ParsedCommand ev = Parser.parse("event project meeting /from 2025-01-01 1400 /to 2025-01-01 1600");
        assertEquals(Parser.CommandType.EVENT, ev.type);
        assertEquals("project meeting", ev.description);
        assertEquals("2025-01-01 1400", ev.start);
        assertEquals("2025-01-01 1600", ev.end);

        // missing description
        assertThrows(NusGptException.class, () -> Parser.parse("event /from 2025-01-01 1400 /to 2025-01-01 1600"));

        // missing /from
        assertThrows(NusGptException.class, () -> Parser.parse("event project meeting /to 2025-01-01 1600"));

        // missing start date
        assertThrows(NusGptException.class, () -> Parser.parse("event project meeting /from /to 2025-01-01 1600"));

        // missing /to
        assertThrows(NusGptException.class, () -> Parser.parse("event project meeting /from 2025-01-01 1400"));

        // missing end date
        assertThrows(NusGptException.class, () -> Parser.parse("event project meeting /from 2025-01-01 1400 /to"));

        // /to before /from (endIndex < startIndex)
        assertThrows(NusGptException.class, () -> Parser.parse("event project meeting /to 2025-01-01 1600 /from 2025-01-01 1400"));

        // invalid date
        assertThrows(NusGptException.class, () -> Parser.parse("event project meeting /from 50/30/2019 1400 /to 50/30/2019 1600"));

        // invalid time
        assertThrows(NusGptException.class, () -> Parser.parse("event project meeting /from 50/30/2019 2500 /to 50/30/2019 3000"));
    }
}
