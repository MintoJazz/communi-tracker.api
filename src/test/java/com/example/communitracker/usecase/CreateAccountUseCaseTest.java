package com.example.communitracker.usecase;

import com.example.communitracker.core.Result;
import com.example.communitracker.domain.Account;
import com.example.communitracker.interfaces.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CreateAccountUseCase createAccountUseCase;

    @Test
    void shouldReturnSuccessWhenCreatingValidAccount() {
        var savedAccount = new Account(1L, "Fundo Comunitário", new BigDecimal("500.00"));
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        Result<Account> result = createAccountUseCase.execute("Fundo Comunitário", new BigDecimal("500.00"));

        assertInstanceOf(Result.Success.class, result);
        var success = (Result.Success<Account>) result;
        assertEquals(1L, success.data().id());
        assertEquals("Fundo Comunitário", success.data().owner());
        assertEquals(new BigDecimal("500.00"), success.data().balance());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldReturnFailureWhenOwnerIsNull() {
        Result<Account> result = createAccountUseCase.execute(null, new BigDecimal("100.00"));

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<Account>) result;
        assertEquals("O nome do titular é obrigatório.", failure.errorMessage());
        verifyNoInteractions(accountRepository);
    }

    @Test
    void shouldReturnFailureWhenOwnerIsBlank() {
        Result<Account> result = createAccountUseCase.execute("   ", new BigDecimal("100.00"));

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<Account>) result;
        assertEquals("O nome do titular é obrigatório.", failure.errorMessage());
        verifyNoInteractions(accountRepository);
    }

    @Test
    void shouldReturnFailureWhenInitialBalanceIsNull() {
        Result<Account> result = createAccountUseCase.execute("Titular Válido", null);

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<Account>) result;
        assertEquals("O saldo inicial não pode ser negativo.", failure.errorMessage());
        verifyNoInteractions(accountRepository);
    }

    @Test
    void shouldReturnFailureWhenInitialBalanceIsNegative() {
        Result<Account> result = createAccountUseCase.execute("Titular Válido", new BigDecimal("-50.00"));

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<Account>) result;
        assertEquals("O saldo inicial não pode ser negativo.", failure.errorMessage());
        verifyNoInteractions(accountRepository);
    }
}
