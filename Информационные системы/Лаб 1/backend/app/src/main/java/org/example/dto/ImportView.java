package org.example.dto;

import org.example.entity.ImportOperation;
import org.example.entity.ImportStatus;

public record ImportView(Long id, ImportStatus status, String user, Integer addedCount,
                         String startedAt, String errorMessage) {

    public static ImportView from(ImportOperation operation) {
        return new ImportView(operation.getId(), operation.getStatus(), operation.getUser().getLogin(),
                operation.getAddedCount(), operation.getStartedAt().toInstant().toString(),
                operation.getErrorMessage());
    }
}
