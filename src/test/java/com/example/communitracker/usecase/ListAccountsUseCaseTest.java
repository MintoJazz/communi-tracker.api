package com.example.communitracker.usecase;

import com.example.communitracker.core.Result;
import com.example.communitracker.domain.Account;
import com.example.communitracker.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAccountsUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ListAccountsUseCase listAccountsUseCase;

    @Test
    void shouldReturnSuccessWhenRepositoryReturnsAccounts() {
        var account = new Account(1L, "Test Owner", BigDecimal.TEN);
        when(accountRepository.getAccounts()).thenReturn(List.of(account));

        Result<List<Account>> result = listAccountsUseCase.execute();

        assertInstanceOf(Result.Success.class, result);
        var successResult = (Result.Success<List<Account>>) result;
        assertEquals(1, successResult.data().size());
        assertEquals("Test Owner", successResult.data().get(0).owner());
    }

    @Test
    void shouldReturnFailureWhenRepositoryReturnsNull() {
        when(accountRepository.getAccounts()).thenReturn(null);

        Result<List<Account>> result = listAccountsUseCase.execute();

        assertInstanceOf(Result.Failure.class, result);
        var failureResult = (Result.Failure<List<Account>>) result;
        assertEquals("Erro ao consultar o banco de dados comunitário.", failureResult.errorMessage());
    }
}
