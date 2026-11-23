package org.example.utils.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.entity.Coordinates;
import org.example.entity.Discipline;
import org.example.entity.LabWork;
import org.example.entity.Location;
import org.example.entity.Person;
import org.example.enums.Color;
import org.example.enums.Country;
import org.example.enums.Difficulty;

@ApplicationScoped
public class ImportFileParser {

    private static final int MAX_BYTES = 2 * 1024 * 1024;
    private static final int MAX_WORKS = 1000;
    private static final List<String> COLUMNS = List.of(
            "name", "coordinates.x", "coordinates.y", "description", "difficulty",
            "minimalPoint", "averagePoint", "discipline.name", "discipline.practiceHours",
            "discipline.selfStudyHours", "discipline.labsCount", "author.name", "author.eyeColor",
            "author.hairColor", "author.location.x", "author.location.y", "author.location.z",
            "author.location.name", "author.birthday", "author.weight", "author.nationality");

    public LabWork[] parse(InputStream file) {
        if (file == null) throw new IllegalArgumentException("Выберите CSV-файл для импорта");
        byte[] bytes;
        try {
            bytes = file.readNBytes(MAX_BYTES + 1);
        } catch (IOException error) {
            throw new IllegalArgumentException("Не удалось прочитать файл", error);
        }
        if (bytes.length == 0) throw new IllegalArgumentException("Файл пуст");
        if (bytes.length > MAX_BYTES) throw new IllegalArgumentException("Размер файла не должен превышать 2 МБ");

        String content;
        try {
            content = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException error) {
            throw new IllegalArgumentException("Файл должен быть в кодировке UTF-8", error);
        }
        if (content.startsWith("\uFEFF")) content = content.substring(1);

        String firstLine = content.split("[\\r\\n]", 2)[0];
        char delimiter = firstLine.contains(";") && !firstLine.contains(",") ? ';' : ',';
        List<List<String>> records = readRecords(content, delimiter);
        if (records.isEmpty()) throw new IllegalArgumentException("CSV-файл пуст");
        Map<String, Integer> header = readHeader(records.get(0));
        List<LabWork> works = new ArrayList<>();
        for (int i = 1; i < records.size(); i++) {
            List<String> row = records.get(i);
            if (row.stream().allMatch(String::isBlank)) continue;
            if (row.size() != header.size()) {
                throw new IllegalArgumentException("Запись " + i + ": ожидалось " + header.size()
                        + " колонок, получено " + row.size() + ". Проверьте разделители и кавычки");
            }
            try {
                works.add(readWork(row, header));
            } catch (IllegalArgumentException error) {
                throw new IllegalArgumentException("Запись " + i + ": " + error.getMessage(), error);
            }
            if (works.size() > MAX_WORKS) {
                throw new IllegalArgumentException("За один раз можно импортировать не больше 1000 работ");
            }
        }
        if (works.isEmpty()) throw new IllegalArgumentException("Файл должен содержать хотя бы одну работу");
        return works.toArray(LabWork[]::new);
    }

    private static Map<String, Integer> readHeader(List<String> cells) {
        Map<String, Integer> header = new HashMap<>();
        for (int i = 0; i < cells.size(); i++) {
            String name = cells.get(i).trim();
            if (!COLUMNS.contains(name)) throw new IllegalArgumentException("Неизвестная колонка CSV: " + name);
            if (header.putIfAbsent(name, i) != null) {
                throw new IllegalArgumentException("Повторяющаяся колонка CSV: " + name);
            }
        }
        for (String name : COLUMNS) {
            if (!header.containsKey(name)) throw new IllegalArgumentException("В CSV отсутствует колонка: " + name);
        }
        return header;
    }

    private static LabWork readWork(List<String> row, Map<String, Integer> header) {
        LabWork work = new LabWork();
        work.setName(value(row, header, "name"));
        Coordinates coordinates = new Coordinates();
        coordinates.setX(requiredFloat(row, header, "coordinates.x"));
        coordinates.setY(optionalInteger(row, header, "coordinates.y"));
        work.setCoordinates(coordinates);
        work.setDescription(value(row, header, "description"));
        work.setDifficulty(optionalEnum(row, header, "difficulty", Difficulty.class));
        work.setMinimalPoint(requiredDouble(row, header, "minimalPoint"));
        work.setAveragePoint(optionalInteger(row, header, "averagePoint"));

        if (hasValues(row, header, "discipline.")) {
            Discipline discipline = new Discipline();
            discipline.setName(value(row, header, "discipline.name"));
            discipline.setPracticeHours(optionalLong(row, header, "discipline.practiceHours"));
            discipline.setSelfStudyHours(requiredLong(row, header, "discipline.selfStudyHours"));
            discipline.setLabsCount(optionalInteger(row, header, "discipline.labsCount"));
            work.setDiscipline(discipline);
        }
        if (hasValues(row, header, "author.")) {
            Person author = new Person();
            author.setName(value(row, header, "author.name"));
            author.setEyeColor(optionalEnum(row, header, "author.eyeColor", Color.class));
            author.setHairColor(optionalEnum(row, header, "author.hairColor", Color.class));
            Location location = new Location();
            location.setX(optionalLong(row, header, "author.location.x"));
            location.setY(optionalDouble(row, header, "author.location.y"));
            location.setZ(requiredLong(row, header, "author.location.z"));
            location.setName(value(row, header, "author.location.name"));
            author.setLocation(location);
            author.setBirthday(optionalDate(row, header, "author.birthday"));
            author.setWeight(optionalLong(row, header, "author.weight"));
            author.setNationality(optionalEnum(row, header, "author.nationality", Country.class));
            work.setAuthor(author);
        }
        return work;
    }

    private static boolean hasValues(List<String> row, Map<String, Integer> header, String prefix) {
        return header.entrySet().stream().anyMatch(entry -> entry.getKey().startsWith(prefix)
                && !row.get(entry.getValue()).isBlank());
    }

    private static String value(List<String> row, Map<String, Integer> header, String name) {
        return row.get(header.get(name));
    }

    private static String requiredValue(List<String> row, Map<String, Integer> header, String name) {
        String value = value(row, header, name).trim();
        if (value.isEmpty()) throw new IllegalArgumentException("Поле " + name + " обязательно");
        return value;
    }

    private static Integer optionalInteger(List<String> row, Map<String, Integer> header, String name) {
        String value = value(row, header, name).trim();
        if (value.isEmpty()) return null;
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Поле " + name + " должно быть целым числом", error);
        }
    }

    private static Long optionalLong(List<String> row, Map<String, Integer> header, String name) {
        String value = value(row, header, name).trim();
        if (value.isEmpty()) return null;
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Поле " + name + " должно быть целым числом", error);
        }
    }

    private static long requiredLong(List<String> row, Map<String, Integer> header, String name) {
        String value = requiredValue(row, header, name);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Поле " + name + " должно быть целым числом", error);
        }
    }

    private static float requiredFloat(List<String> row, Map<String, Integer> header, String name) {
        String value = requiredValue(row, header, name).replace(',', '.');
        try {
            float number = Float.parseFloat(value);
            if (Float.isFinite(number)) return number;
        } catch (NumberFormatException ignored) {
        }
        throw new IllegalArgumentException("Поле " + name + " должно быть конечным числом");
    }

    private static double requiredDouble(List<String> row, Map<String, Integer> header, String name) {
        Double value = optionalDouble(row, header, name);
        if (value == null) throw new IllegalArgumentException("Поле " + name + " обязательно");
        return value;
    }

    private static Double optionalDouble(List<String> row, Map<String, Integer> header, String name) {
        String value = value(row, header, name).trim().replace(',', '.');
        if (value.isEmpty()) return null;
        try {
            double number = Double.parseDouble(value);
            if (Double.isFinite(number)) return number;
        } catch (NumberFormatException ignored) {
        }
        throw new IllegalArgumentException("Поле " + name + " должно быть конечным числом");
    }

    private static <T extends Enum<T>> T optionalEnum(List<String> row, Map<String, Integer> header,
            String name, Class<T> type) {
        String value = value(row, header, name).trim();
        if (value.isEmpty()) return null;
        try {
            return Enum.valueOf(type, value);
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("Поле " + name + ": допустимые значения "
                    + Arrays.toString(type.getEnumConstants()), error);
        }
    }

    private static Date optionalDate(List<String> row, Map<String, Integer> header, String name) {
        String value = value(row, header, name).trim();
        if (value.isEmpty()) return null;
        try {
            return Date.from(OffsetDateTime.parse(value).toInstant());
        } catch (DateTimeParseException ignored) {
            try {
                return Date.from(LocalDate.parse(value).atStartOfDay(ZoneOffset.UTC).toInstant());
            } catch (DateTimeParseException error) {
                throw new IllegalArgumentException("Поле " + name
                        + " должно быть датой YYYY-MM-DD или ISO-датой со временем и часовым поясом", error);
            }
        }
    }

    private static List<List<String>> readRecords(String content, char delimiter) {
        List<List<String>> records = new ArrayList<>();
        List<String> row = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        int state = 0; 
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (state == 2) {
                if (c == '"') {
                    if (i + 1 < content.length() && content.charAt(i + 1) == '"') {
                        field.append('"');
                        i++;
                    } else {
                        state = 3;
                    }
                } else {
                    field.append(c);
                }
                continue;
            }
            if (c == delimiter) {
                row.add(field.toString());
                field.setLength(0);
                state = 0;
            } else if (c == '\n' || c == '\r') {
                row.add(field.toString());
                records.add(row);
                row = new ArrayList<>();
                field.setLength(0);
                state = 0;
                if (c == '\r' && i + 1 < content.length() && content.charAt(i + 1) == '\n') i++;
            } else if (c == '"' && state == 0) {
                state = 2;
            } else if (c == '"' || state == 3) {
                throw new IllegalArgumentException("Некорректный CSV: проверьте кавычки в записи "
                        + (records.size() + 1));
            } else {
                field.append(c);
                state = 1;
            }
        }
        if (state == 2) throw new IllegalArgumentException("Некорректный CSV: незакрытая кавычка");
        if (!row.isEmpty() || field.length() > 0 || state != 0) {
            row.add(field.toString());
            records.add(row);
        }
        return records;
    }
}
