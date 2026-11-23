package org.example.utils.cdi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ImportFileParserTest {

    private final ImportFileParser parser = new ImportFileParser();

    @Test
    void parsesDocumentedExampleWithInlineReferences() throws Exception {
        byte[] file = Files.readAllBytes(Path.of("../../examples/import-labworks.csv"));
        var works = parser.parse(new ByteArrayInputStream(file));

        assertEquals(2, works.length);
        assertEquals("Информационные системы", works[0].getDiscipline().getName());
        assertEquals("Иван", works[0].getAuthor().getName());
        assertEquals("Санкт-Петербург", works[0].getAuthor().getLocation().getName());
        assertEquals("Первая работа, \"CSV\"", works[0].getDescription());
        assertEquals(1.5, works[0].getMinimalPoint());
        assertEquals(2.5, works[0].getAuthor().getLocation().getY());
        assertEquals(null, works[1].getAuthor());
    }

    @Test
    void reportsIndexOfBadRecord() throws Exception {
        String file = Files.readString(Path.of("../../examples/import-labworks.csv"))
                .replace("INSANE", "UNKNOWN");
        var error = assertThrows(IllegalArgumentException.class,
                () -> parser.parse(new ByteArrayInputStream(file.getBytes(StandardCharsets.UTF_8))));
        assertTrue(error.getMessage().contains("Запись 2"));
        assertTrue(error.getMessage().contains("difficulty"));
    }

    @Test
    void acceptsSemicolonSeparatedRowsWithDecimalComma() throws Exception {
        String header = Files.readAllLines(Path.of("../../examples/import-labworks.csv")).get(0)
                .replace(',', ';');
        String row = String.join(";", "\"CSV; тест\"", "\"1,5\"", "2", "\"Описание, \"\"пример\"\"\"", "EASY", "1,5", "2",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "");
        var works = parser.parse(new ByteArrayInputStream((header + "\n" + row).getBytes(StandardCharsets.UTF_8)));
        assertEquals(1, works.length);
        assertEquals("CSV; тест", works[0].getName());
        assertEquals("Описание, \"пример\"", works[0].getDescription());
        assertEquals(1.5, works[0].getMinimalPoint());
    }

    @Test
    void rejectsUnclosedQuotes() throws Exception {
        String header = Files.readAllLines(Path.of("../../examples/import-labworks.csv")).get(0);
        String file = header + "\n\"незакрытая строка";
        var error = assertThrows(IllegalArgumentException.class,
                () -> parser.parse(new ByteArrayInputStream(file.getBytes(StandardCharsets.UTF_8))));
        assertTrue(error.getMessage().contains("незакрытая кавычка"));
    }
}
