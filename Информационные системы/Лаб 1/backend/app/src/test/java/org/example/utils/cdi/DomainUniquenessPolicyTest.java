package org.example.utils.cdi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.transaction.Status;
import jakarta.transaction.Synchronization;
import jakarta.transaction.TransactionSynchronizationRegistry;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.List;
import org.example.utils.repository.LabWorkRepository;
import org.example.utils.repository.ReferenceRepository;
import org.example.entity.Coordinates;
import org.junit.jupiter.api.Test;

class DomainUniquenessPolicyTest {

    @Test
    void namesUseCaseInsensitiveTrimmedComparison() throws Exception {
        DomainUniquenessPolicy policy = new DomainUniquenessPolicy();
        inject(policy, "works", new LabWorkRepository() {
            @Override public java.util.List<String> namesExceptId(Integer exceptId) {
                return java.util.List.of("  Java  ");
            }
        });
        assertThrows(DuplicateLabWorkNameException.class,
                () -> policy.requireWorkNameAvailable("java", null));
    }

    @Test
    void coordinateAndReferenceRulesRejectExistingValues() throws Exception {
        DomainUniquenessPolicy policy = new DomainUniquenessPolicy();
        inject(policy, "works", new LabWorkRepository() {
            @Override public boolean existsByCoordinatesExceptId(float x, Integer y, Integer exceptId) {
                return x == 1.5f && y == 2;
            }
            @Override public boolean existsByAuthorDisciplineExceptId(
                    Long authorId, Long disciplineId, Integer exceptId) {
                return authorId == 10 && disciplineId == 20;
            }
        });
        inject(policy, "references", new ReferenceRepository() {
            @Override public java.util.List<String> disciplineNamesExceptId(Long exceptId) {
                return java.util.List.of("  Databases  ");
            }
        });
        Coordinates coordinates = new Coordinates();
        coordinates.setX(1.5f);
        coordinates.setY(2);
        assertThrows(DuplicateValueException.class,
                () -> policy.requireCoordinatesAvailable(coordinates, null));
        assertThrows(DuplicateValueException.class,
                () -> policy.requireDisciplineNameAvailable("databases", null));
        assertThrows(DuplicateValueException.class,
                () -> policy.requireAuthorDisciplineAvailable(10L, 20L, null));
    }

    @Test
    void concurrentCreationWaitsForCommitBeforeCheckingName() throws Exception {
        Set<String> committedNames = ConcurrentHashMap.newKeySet();
        var callbacks = new ConcurrentHashMap<Thread, Synchronization>();
        TransactionSynchronizationRegistry transactions = (TransactionSynchronizationRegistry) Proxy.newProxyInstance(
                TransactionSynchronizationRegistry.class.getClassLoader(),
                new Class<?>[] { TransactionSynchronizationRegistry.class },
                (proxy, method, args) -> {
                    if (method.getName().equals("registerInterposedSynchronization")) {
                        callbacks.put(Thread.currentThread(), (Synchronization) args[0]);
                        return null;
                    }
                    throw new UnsupportedOperationException(method.getName());
                });
        DomainUniquenessPolicy policy = new DomainUniquenessPolicy();
        inject(policy, "transactions", transactions);
        inject(policy, "works", new LabWorkRepository() {
            @Override
            public List<String> namesExceptId(Integer exceptId) {
                return List.copyOf(committedNames);
            }
        });

        CountDownLatch firstReady = new CountDownLatch(1);
        CountDownLatch commitFirst = new CountDownLatch(1);
        CountDownLatch secondStarted = new CountDownLatch(1);
        ExecutorService workers = Executors.newFixedThreadPool(2);
        try {
            var first = workers.submit(() -> {
                policy.lockUntilTransactionEnds();
                policy.requireWorkNameAvailable("Same name", null);
                firstReady.countDown();
                if (!commitFirst.await(5, TimeUnit.SECONDS)) throw new AssertionError("First commit was never released");
                committedNames.add("Same name");
                callbacks.get(Thread.currentThread()).afterCompletion(Status.STATUS_COMMITTED);
                return true;
            });
            var second = workers.submit(() -> {
                if (!firstReady.await(5, TimeUnit.SECONDS)) throw new AssertionError("First transaction did not start");
                secondStarted.countDown();
                policy.lockUntilTransactionEnds();
                try {
                    policy.requireWorkNameAvailable("same NAME ", null);
                    return true;
                } finally {
                    callbacks.get(Thread.currentThread()).afterCompletion(Status.STATUS_ROLLEDBACK);
                }
            });
            try {
                assertTrue(firstReady.await(5, TimeUnit.SECONDS));
                assertTrue(secondStarted.await(5, TimeUnit.SECONDS));
                assertThrows(TimeoutException.class, () -> second.get(150, TimeUnit.MILLISECONDS));
            } finally {
                commitFirst.countDown();
            }
            assertTrue(first.get(5, TimeUnit.SECONDS));
            ExecutionException conflict = assertThrows(ExecutionException.class,
                    () -> second.get(5, TimeUnit.SECONDS));
            assertEquals(DuplicateLabWorkNameException.class, conflict.getCause().getClass());
            assertEquals(1, committedNames.size());
        } finally {
            commitFirst.countDown();
            workers.shutdownNow();
        }
    }

    private static void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
