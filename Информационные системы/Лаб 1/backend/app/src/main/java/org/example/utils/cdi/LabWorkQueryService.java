package org.example.utils.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.example.entity.LabWork;
import org.example.utils.repository.LabWorkRepository;

@ApplicationScoped
public class LabWorkQueryService {

    @Inject
    private LabWorkRepository repository;

    public List<LabWork> list(String filterField, String filterValue, String sortField, String direction) {
        List<LabWork> works = new ArrayList<>(repository.findAll());
        filterWorks(works, filterField, filterValue);
        sortWorks(works, sortField, direction);
        return works;
    }

    private static void filterWorks(List<LabWork> works, String filterField, String filterValue) {
        if (filterValue == null || filterValue.isBlank()) {
            return;
        }
        String field = requireStringField(filterField);
        String needle = filterValue.toLowerCase(Locale.ROOT);
        works.removeIf(work -> !stringValue(work, field).toLowerCase(Locale.ROOT).contains(needle));
    }

    private static void sortWorks(List<LabWork> works, String sortField, String direction) {
        if (sortField == null || sortField.isBlank()) {
            return;
        }
        String field = requireStringField(sortField);
        Comparator<LabWork> comparator = Comparator.comparing(
                work -> stringValue(work, field), String.CASE_INSENSITIVE_ORDER);
        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        } else if (direction != null && !direction.isBlank() && !"asc".equalsIgnoreCase(direction)) {
            throw new IllegalArgumentException("Направление сортировки: asc или desc");
        }
        works.sort(comparator.thenComparingInt(LabWork::getId));
    }

    private static String requireStringField(String field) {
        if (field == null) {
            throw new IllegalArgumentException("Укажите строковое поле для фильтрации");
        }
        switch (field) {
            case "name", "description", "difficulty", "discipline.name", "author.name",
                    "author.eyeColor", "author.hairColor", "author.nationality", "author.location.name" -> { return field; }
            default -> throw new IllegalArgumentException("Неизвестное строковое поле: " + field);
        }
    }

    private static String stringValue(LabWork value, String field) {
        return switch (field) {
            case "name" -> value.getName();
            case "description" -> value.getDescription();
            case "difficulty" -> value.getDifficulty().name();
            case "discipline.name" -> value.getDiscipline() == null ? "" : value.getDiscipline().getName();
            case "author.name" -> value.getAuthor() == null ? "" : value.getAuthor().getName();
            case "author.eyeColor" -> value.getAuthor() == null ? "" : value.getAuthor().getEyeColor().name();
            case "author.hairColor" -> value.getAuthor() == null || value.getAuthor().getHairColor() == null
                    ? "" : value.getAuthor().getHairColor().name();
            case "author.nationality" -> value.getAuthor() == null ? "" : value.getAuthor().getNationality().name();
            case "author.location.name" -> value.getAuthor() == null ? "" : value.getAuthor().getLocation().getName();
            default -> "";
        };
    }
}
