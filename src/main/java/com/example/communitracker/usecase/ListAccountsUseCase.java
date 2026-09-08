package com.example.communitracker.usecase;

import com.example.communitracker.core.Result;
import com.example.communitracker.domain.Account;
import com.example.communitracker.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListAccountsUseCase {
    
    private final AccountRepository accountRepository;

    public ListAccountsUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Result<List<Account>> execute() {
        List<Account> accounts = accountRepository.getAccounts();
        
        if (accounts == null) return new Result.Failure<>("Erro ao consultar o banco de dados comunitário.");
        
        return new Result.Success<>(accounts);
    }
}