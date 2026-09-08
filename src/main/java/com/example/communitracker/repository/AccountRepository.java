package com.example.communitracker.repository;

import java.util.List;

import com.example.communitracker.domain.Account;

public interface AccountRepository {
    List<Account> getAccounts();
}
