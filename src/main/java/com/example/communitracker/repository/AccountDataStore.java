package com.example.communitracker.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.example.communitracker.domain.Account;

@Component
public class AccountDataStore {
    // Fonte de dados única e compartilhada na memória
    public final List<Account> accounts = new ArrayList<>(List.of(
        new Account(1L, "Fundo Solidário Principal", new BigDecimal("15000.00")),
        new Account(2L, "Empreendimento Local", new BigDecimal("350.50"))
    ));
}