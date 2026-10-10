package com.example.communitracker.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.example.communitracker.domain.Account;
import com.example.communitracker.interfaces.AccountQueries;

@Repository
public class AccountQueriesMock implements AccountQueries {
    private final AccountDataStore db;

    public AccountQueriesMock(AccountDataStore db) {
        this.db = db;
    }

    @Override
    public List<Account> getAccounts() {
        return new ArrayList<>(db.accounts);
    }

    @Override
    public Optional<Account> getAccount(Long id) {
        return db.accounts.stream()
                .filter(account -> account.id().equals(id))
                .findFirst();
    }
}