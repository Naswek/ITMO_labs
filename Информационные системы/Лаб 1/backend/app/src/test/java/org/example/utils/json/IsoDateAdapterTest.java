package org.example.utils.json;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IsoDateAdapterTest {

    private final IsoDateAdapter adapter = new IsoDateAdapter();

    @Test
    void rejectsNonexistentCalendarDate() {
        assertThrows(DateTimeParseException.class,
                () -> adapter.adaptFromJson("2023-02-30T00:00:00.000Z"));
        assertThrows(DateTimeParseException.class,
                () -> adapter.adaptFromJson("2023-02-29T00:00:00.000Z"));
    }

    @Test
    void acceptsLeapDayAndPreservesMilliseconds() {
        Date expected = Date.from(Instant.parse("2024-02-29T12:34:56.123Z"));
        assertEquals(expected, adapter.adaptFromJson("2024-02-29T12:34:56.123Z"));
        assertEquals("2024-02-29T12:34:56.123Z", adapter.adaptToJson(expected));
    }

    @Test
    void normalizesTimezoneOffsets() {
        Date expected = Date.from(Instant.parse("2002-07-08T00:00:00Z"));
        assertEquals(expected, adapter.adaptFromJson("2002-07-08T03:00:00+03:00"));
        assertEquals("2002-07-08T00:00:00Z", adapter.adaptToJson(expected));
    }
}
