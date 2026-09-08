package com.example.communitracker.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.communitracker.domain.Account;

@Repository
public class AccountRepositoryMock implements AccountRepository {
    @Override
    public List<Account> getAccounts() {
        return List.of(
            new Account(1L, "Fundo Solidário Principal", new BigDecimal("15000.00")),
            new Account(2L, "Empreendimento Local", new BigDecimal("350.50"))
        );
    }

}
