package org.example.utils.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.InputStream;
import org.example.dto.ImportView;
import org.example.entity.LabWork;

@ApplicationScoped
public class ImportService {

    @Inject private ImportFileParser parser;
    @Inject private ImportBatchService batch;
    @Inject private ImportHistoryService history;

    public ImportView importFile(long userId, InputStream file) {
        try {
            LabWork[] values = parser.parse(file);
            return batch.importWorks(userId, values);
        } catch (Exception error) {
            history.recordFailure(userId, error.getMessage());
            if (error instanceof RuntimeException runtime) throw runtime;
            throw new IllegalStateException("Не удалось выполнить импорт", error);
        }
    }
}
