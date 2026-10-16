package com.example.communitracker.repository;

import com.example.communitracker.domain.CreditRequest;
import com.example.communitracker.domain.CreditRequestStatus;
import com.example.communitracker.domain.SolidarityEnterprise;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreditRequestDataStore {
    // Fonte de dados única e compartilhada na memória
    public final List<CreditRequest> creditRequests = new ArrayList<>(List.of(
        new CreditRequest(
            1L,
            new SolidarityEnterprise(
                "Cooperativa Agroecológica Terra Viva",
                "01.234.567/0001-89",
                "Assentamento Bela Vista",
                "Agroecologia"
            ),
            new BigDecimal("5000.00"),
            18,
            BigDecimal.ZERO,
            CreditRequestStatus.UNDER_ANALYSIS,
            LocalDateTime.of(2026, 10, 10, 10, 0),
            "Aquisição de sistema de irrigação por gotejamento"
        )
    ));
}
