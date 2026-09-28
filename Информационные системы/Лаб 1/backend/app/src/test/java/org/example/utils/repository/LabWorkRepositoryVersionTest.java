package org.example.utils.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;
import org.example.entity.LabWork;
import org.junit.jupiter.api.Test;

class LabWorkRepositoryVersionTest {

    @Test
    void staleUpdateDoesNotOverwriteCurrentValues() throws Exception {
        LabWork current = work(1, 4L, "Новое название");
        LabWorkRepository repository = repository(current, new AtomicBoolean());

        assertThrows(StaleObjectException.class, () -> repository.update(work(1, 3L, "Старое название")));
        assertEquals("Новое название", current.getName());
    }

    @Test
    void staleDeleteDoesNotRemoveCurrentRecord() throws Exception {
        AtomicBoolean removed = new AtomicBoolean();
        LabWorkRepository repository = repository(work(1, 4L, "Работа"), removed);

        assertThrows(StaleObjectException.class, () -> repository.deleteById(1, 3L));
        assertFalse(removed.get());
        assertTrue(repository.deleteById(1, 4L));
        assertTrue(removed.get());
    }

    private static LabWork work(int id, Long version, String name) {
        LabWork value = new LabWork();
        value.setId(id);
        value.setVersion(version);
        value.setName(name);
        return value;
    }

    private static LabWorkRepository repository(LabWork current, AtomicBoolean removed) throws Exception {
        EntityManager manager = (EntityManager) Proxy.newProxyInstance(
                EntityManager.class.getClassLoader(), new Class<?>[] { EntityManager.class },
                (proxy, method, arguments) -> switch (method.getName()) {
                    case "find" -> current;
                    case "remove" -> { removed.set(true); yield null; }
                    default -> throw new UnsupportedOperationException(method.getName());
                });
        LabWorkRepository repository = new LabWorkRepository();
        Field field = LabWorkRepository.class.getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(repository, manager);
        return repository;
    }
}
