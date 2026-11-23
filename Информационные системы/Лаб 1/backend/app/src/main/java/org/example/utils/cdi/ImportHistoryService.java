package org.example.utils.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.example.dto.ImportView;
import org.example.entity.ImportOperation;
import org.example.entity.ImportStatus;
import org.example.entity.UserEntity;
import org.example.utils.repository.ImportHistoryRepository;

@ApplicationScoped
public class ImportHistoryService {

    @Inject
    private ImportHistoryRepository repository;

    @Inject
    private UserService users;

    public List<ImportView> list(long userId) {
        UserEntity user = users.getUserById(userId);
        return repository.list(userId, user.isAdmin()).stream().map(ImportView::from).toList();
    }

    @Transactional
    public void recordFailure(long userId, String message) {
        ImportOperation operation = new ImportOperation();
        operation.setUser(users.getUserById(userId));
        operation.setStatus(ImportStatus.FAILED);
        if (message != null) operation.setErrorMessage(message.substring(0, Math.min(500, message.length())));
        repository.create(operation);
    }
}
