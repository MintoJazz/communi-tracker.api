package com.example.communitracker.repository;

import com.example.communitracker.domain.CreditRequest;
import com.example.communitracker.domain.CreditRequestStatus;
import com.example.communitracker.domain.SolidarityEnterprise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreditRequestRepositoryImplTest {

    private CreditRequestDataStore db;
    private CreditRequestRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        db = new CreditRequestDataStore();
        repository = new CreditRequestRepositoryImpl(db);
    }

    @Test
    void shouldSaveCreditRequestAndGenerateNextId() {
        int initialSize = db.creditRequests.size();
        var enterprise = new SolidarityEnterprise("Costura Solidária", "11.222.333/0001-44", "Zona Norte", "Moda Popular");
        var request = new CreditRequest(
            null,
            enterprise,
            new BigDecimal("2500.00"),
            10,
            BigDecimal.ZERO,
            CreditRequestStatus.UNDER_ANALYSIS,
            LocalDateTime.now(),
            "Compra de tecidos"
        );

        CreditRequest saved = repository.save(request);

        assertNotNull(saved.id());
        assertEquals(Long.valueOf(initialSize + 1), saved.id());
        assertEquals(enterprise, saved.enterprise());
        assertEquals(new BigDecimal("2500.00"), saved.requestedAmount());
        assertEquals(10, saved.installmentMonths());
        assertEquals(BigDecimal.ZERO, saved.interestRate());
        assertEquals(CreditRequestStatus.UNDER_ANALYSIS, saved.status());
        assertEquals(initialSize + 1, db.creditRequests.size());
    }

    @Test
    void shouldFindByIdWhenExists() {
        Optional<CreditRequest> found = repository.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().id());
        assertEquals("Cooperativa Agroecológica Terra Viva", found.get().enterprise().name());
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        Optional<CreditRequest> found = repository.findById(999L);

        assertTrue(found.isEmpty());
    }
}
