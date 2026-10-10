package com.example.communitracker.repository;

import com.example.communitracker.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountRepositoryMockTest {

    private AccountDataStore db;
    private AccountRepositoryMock repositoryMock;

    @BeforeEach
    void setUp() {
        db = new AccountDataStore();
        repositoryMock = new AccountRepositoryMock(db);
    }

    @Test
    void shouldSaveNewAccountAndGenerateNextId() {
        int initialSize = db.accounts.size();
        Account newAccount = new Account(null, "Novo Fundo Solidário", new BigDecimal("750.00"));

        Account saved = repositoryMock.save(newAccount);

        assertNotNull(saved.id());
        assertEquals(Long.valueOf(initialSize + 1), saved.id());
        assertEquals("Novo Fundo Solidário", saved.owner());
        assertEquals(new BigDecimal("750.00"), saved.balance());
        assertEquals(initialSize + 1, db.accounts.size());
        assertEquals(saved, db.accounts.get(db.accounts.size() - 1));
    }
}
