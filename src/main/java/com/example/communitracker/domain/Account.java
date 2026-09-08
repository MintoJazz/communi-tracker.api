package com.example.communitracker.domain;

import java.math.BigDecimal;

public record Account(Long id, String owner, BigDecimal balance) {}