package com.example.communitracker.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreditRequest(
    Long id,
    SolidarityEnterprise enterprise,
    BigDecimal requestedAmount,
    Integer installmentMonths,
    BigDecimal interestRate,
    CreditRequestStatus status,
    LocalDateTime requestedAt,
    String purpose
) {}
