package com.example.communitracker.usecase;

import com.example.communitracker.core.Result;
import com.example.communitracker.domain.Account;
import com.example.communitracker.interfaces.AccountQueries;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAccountUseCaseTest {

    @Mock
    private AccountQueries accountQueries;

    @InjectMocks
    private GetAccountUseCase getAccountUseCase;

    @Test
    void shouldReturnSuccessWhenRepositoryReturnsAccount() {
        var account = new Account(1L, "Test Owner", BigDecimal.TEN);
        when(accountQueries.getAccount(1L)).thenReturn(Optional.of(account));

        Result<Account> result = getAccountUseCase.execute(1L);

        assertInstanceOf(Result.Success.class, result);
        var successResult = (Result.Success<Account>) result;
        assertEquals(1L, successResult.data().id());
        assertEquals("Test Owner", successResult.data().owner());
    }

    @Test
    void shouldReturnFailureWhenRepositoryReturnsEmpty() {
        when(accountQueries.getAccount(1L)).thenReturn(Optional.empty());

        Result<Account> result = getAccountUseCase.execute(1L);

        assertInstanceOf(Result.Failure.class, result);
        var failureResult = (Result.Failure<Account>) result;
        assertEquals("Conta não encontrada.", failureResult.errorMessage());
    }
}

