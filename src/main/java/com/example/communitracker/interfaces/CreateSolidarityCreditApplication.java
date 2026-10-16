package com.example.communitracker.interfaces;

import com.example.communitracker.core.Result;
import com.example.communitracker.domain.CreditRequest;

import java.math.BigDecimal;

public interface CreateSolidarityCreditApplication {
    record Command(
        String enterpriseName,
        String document,
        String communityLocation,
        String segment,
        BigDecimal requestedAmount,
        Integer installmentMonths,
        String purpose
    ) {}

    Result<CreditRequest> execute(Command command);
}
