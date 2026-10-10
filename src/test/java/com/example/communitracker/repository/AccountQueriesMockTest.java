package com.example.communitracker.repository;

import com.example.communitracker.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountQueriesMockTest {

    private AccountDataStore db;
    private AccountQueriesMock queriesMock;

    @BeforeEach
    void setUp() {
        db = new AccountDataStore();
        queriesMock = new AccountQueriesMock(db);
    }

    @Test
    void shouldReturnAllAccounts() {
        List<Account> accounts = queriesMock.getAccounts();

        assertEquals(2, accounts.size());
        assertEquals("Fundo Solidário Principal", accounts.get(0).owner());
        assertEquals("Empreendimento Local", accounts.get(1).owner());
    }

    @Test
    void shouldReturnAccountWhenIdExists() {
        Optional<Account> account = queriesMock.getAccount(1L);

        assertTrue(account.isPresent());
        assertEquals(1L, account.get().id());
        assertEquals("Fundo Solidário Principal", account.get().owner());
    }

    @Test
    void shouldReturnEmptyWhenIdDoesNotExist() {
        Optional<Account> account = queriesMock.getAccount(999L);

        assertTrue(account.isEmpty());
    }
}
