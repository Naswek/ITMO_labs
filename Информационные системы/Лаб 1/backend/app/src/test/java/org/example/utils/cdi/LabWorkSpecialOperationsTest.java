package org.example.utils.cdi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import org.example.entity.LabWork;
import org.example.enums.Difficulty;
import org.example.utils.repository.LabWorkRepository;
import org.junit.jupiter.api.Test;

class LabWorkSpecialOperationsTest {

    @Test
    void computesSpecialResultsInBusinessLayer() throws Exception {
        List<LabWork> works = List.of(work("Java", Difficulty.EASY),
                work("Java EE", Difficulty.HOPELESS), work("SQL", Difficulty.EASY));
        LabWorkService service = new LabWorkService();
        Field repository = LabWorkService.class.getDeclaredField("repository");
        repository.setAccessible(true);
        repository.set(service, new LabWorkRepository() {
            @Override
            public List<LabWork> findAll() {
                return works;
            }
        });

        assertEquals("Java EE", service.maximumDifficulty().orElseThrow().getName());
        assertEquals(2L, service.countByDifficulty().get(Difficulty.EASY));
        assertEquals(0L, service.countByDifficulty().get(Difficulty.IMPOSSIBLE));
        assertEquals(List.of("Java", "Java EE"),
                service.nameContains("Java").stream().map(LabWork::getName).toList());
        assertTrue(service.nameContains("python").isEmpty());
    }

    private static LabWork work(String name, Difficulty difficulty) {
        LabWork value = new LabWork();
        value.setName(name);
        value.setDifficulty(difficulty);
        return value;
    }
}
