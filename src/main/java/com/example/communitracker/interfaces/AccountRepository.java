package com.example.communitracker.interfaces;

import com.example.communitracker.domain.Account;

public interface AccountRepository {
    Account save(Account account);
}