package org.example.utils.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import org.example.dto.ImportView;
import org.example.entity.Discipline;
import org.example.entity.ImportOperation;
import org.example.entity.ImportStatus;
import org.example.entity.LabWork;
import org.example.entity.Person;
import org.example.utils.cdi.validators.DisciplineValidator;
import org.example.utils.cdi.validators.LabWorkValidator;
import org.example.utils.cdi.validators.PersonValidator;
import org.example.utils.repository.ImportHistoryRepository;
import org.example.utils.repository.LabWorkRepository;
import org.example.utils.repository.ReferenceRepository;

@ApplicationScoped
public class ImportBatchService {

    @Inject private LabWorkValidator workValidator;
    @Inject private DisciplineValidator disciplineValidator;
    @Inject private PersonValidator personValidator;
    @Inject private LabWorkRepository works;
    @Inject private ReferenceRepository references;
    @Inject private ImportHistoryRepository history;
    @Inject private UserService users;
    @Inject private Event<CollectionChanged> changes;
    @Inject private DomainUniquenessPolicy uniqueness;

    @Transactional
    public ImportView importWorks(long userId, LabWork[] values) {
        for (int i = 0; i < values.length; i++) {
            try {
                LabWork work = values[i];
                workValidator.validateForImport(work);
                Discipline discipline = work.getDiscipline();
                Person author = work.getAuthor();
                if (discipline != null) disciplineValidator.validateForCreate(discipline);
                if (author != null) personValidator.validateForCreate(author);
            } catch (IllegalArgumentException error) {
                throw new IllegalArgumentException("Запись " + (i + 1) + ": " + error.getMessage(), error);
            }
        }

        uniqueness.lockUntilTransactionEnds();
        Set<String> names = new HashSet<>();
        Set<DomainUniquenessPolicy.CoordinatesKey> coordinates = new HashSet<>();
        Set<String> disciplineNames = new HashSet<>();
        for (int i = 0; i < values.length; i++) {
            LabWork work = values[i];
            String name = work.getName();
            if (!names.add(DomainUniquenessPolicy.nameKey(name))) {
                throw new DuplicateLabWorkNameException("Запись " + (i + 1)
                        + ": название «" + name + "» повторяется в файле");
            }
            uniqueness.requireWorkNameAvailable(name, null);
            if (!coordinates.add(DomainUniquenessPolicy.coordinatesKey(work.getCoordinates()))) {
                throw new DuplicateValueException("Запись " + (i + 1)
                        + ": координаты повторяются в файле");
            }
            uniqueness.requireCoordinatesAvailable(work.getCoordinates(), null);
            if (work.getDiscipline() != null) {
                String disciplineName = work.getDiscipline().getName();
                if (!disciplineNames.add(DomainUniquenessPolicy.nameKey(disciplineName))) {
                    throw new DuplicateValueException("Запись " + (i + 1)
                            + ": название дисциплины повторяется в файле");
                }
                uniqueness.requireDisciplineNameAvailable(disciplineName, null);
            }
        }

        for (LabWork work : values) {
            if (work.getDiscipline() != null) work.setDiscipline(references.create(work.getDiscipline()));
            if (work.getAuthor() != null) work.setAuthor(references.create(work.getAuthor()));
            if (work.getAuthor() != null && work.getDiscipline() != null) {
                uniqueness.requireAuthorDisciplineAvailable(
                        work.getAuthor().getId(), work.getDiscipline().getId(), null);
            }
            works.create(work);
        }

        ImportOperation operation = new ImportOperation();
        operation.setUser(users.getUserById(userId));
        operation.setStatus(ImportStatus.SUCCESS);
        operation.setAddedCount(values.length);
        history.create(operation);
        history.flush();
        changes.fire(new CollectionChanged());
        return ImportView.from(operation);
    }
}
