package com.example.communitracker.repository;

import org.springframework.stereotype.Repository;
import com.example.communitracker.domain.Account;
import com.example.communitracker.interfaces.AccountRepository;

@Repository public class AccountRepositoryMock implements AccountRepository {
    private final AccountDataStore db;

    public AccountRepositoryMock(AccountDataStore db) {
        this.db = db;
    }

    @Override
    public Account save(Account account) {
        Long newId = (long) (db.accounts.size() + 1);
        Account newAccount = new Account(newId, account.owner(), account.balance());
        db.accounts.add(newAccount);
        return newAccount;
    }
}