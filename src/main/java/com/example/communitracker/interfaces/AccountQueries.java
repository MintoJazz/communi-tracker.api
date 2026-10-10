package com.example.communitracker.interfaces;

import java.util.List;
import java.util.Optional;
import com.example.communitracker.domain.Account;

public interface AccountQueries {
    List<Account> getAccounts();
    Optional<Account> getAccount(Long id);
}