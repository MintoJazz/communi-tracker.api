package com.example.communitracker.usecase;

import com.example.communitracker.core.Result;
import com.example.communitracker.domain.Account;
import com.example.communitracker.interfaces.AccountRepository;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class CreateAccountUseCase {
    private final AccountRepository accountRepository;

    public CreateAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Result<Account> execute(String owner, BigDecimal initialBalance) {
        if (owner == null || owner.isBlank()) {
            return new Result.Failure<>("O nome do titular é obrigatório.");
        }
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            return new Result.Failure<>("O saldo inicial não pode ser negativo.");
        }

        Account newAccount = new Account(null, owner, initialBalance);
        Account savedAccount = accountRepository.save(newAccount);
        
        return new Result.Success<>(savedAccount);
    }
}