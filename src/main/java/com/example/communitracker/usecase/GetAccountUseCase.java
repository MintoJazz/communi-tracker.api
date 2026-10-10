package com.example.communitracker.usecase;

import com.example.communitracker.core.Result;
import com.example.communitracker.domain.Account;
import com.example.communitracker.interfaces.AccountQueries;

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class GetAccountUseCase {
    private final AccountQueries accountQueries;

    public GetAccountUseCase(AccountQueries accountQueries) {
        this.accountQueries = accountQueries;
    }

    public Result<Account> execute(Long id) {
        Optional<Account> account = accountQueries.getAccount(id);
        if (account.isEmpty()) return new Result.Failure<>("Conta não encontrada.");
        return new Result.Success<>(account.get());
    }
}