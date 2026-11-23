package org.example.utils.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Locale;
import org.example.dto.LabWorkPage;
import org.example.utils.repository.LabWorkRepository;

@ApplicationScoped
public class LabWorkQueryService {

    @Inject
    private LabWorkRepository repository;

    public LabWorkPage list(String filterField, String filterValue, String sortField, String direction,
            int page, int size) {
        if (page < 0) throw new IllegalArgumentException("Номер страницы не может быть отрицательным");
        if (size < 1 || size > 100) throw new IllegalArgumentException("Размер страницы должен быть от 1 до 100");

        String filteredBy = filterValue == null || filterValue.isBlank() ? null : requireStringField(filterField);
        String sortedBy = sortField == null || sortField.isBlank() ? null : requireStringField(sortField);
        String order = "asc";
        if (sortedBy != null && direction != null && !direction.isBlank()) {
            if (!"asc".equalsIgnoreCase(direction) && !"desc".equalsIgnoreCase(direction)) {
                throw new IllegalArgumentException("Направление сортировки: asc или desc");
            }
            order = direction.toLowerCase(Locale.ROOT);
        }
        return repository.findPage(filteredBy, filterValue, sortedBy, order, page, size);
    }

    private static String requireStringField(String field) {
        if (field == null) throw new IllegalArgumentException("Укажите строковое поле для фильтрации");
        return switch (field) {
            case "name", "description", "difficulty", "discipline.name", "author.name",
                    "author.eyeColor", "author.hairColor", "author.nationality", "author.location.name" -> field;
            default -> throw new IllegalArgumentException("Неизвестное строковое поле: " + field);
        };
    }
}
