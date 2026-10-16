package com.example.communitracker.usecase;

import com.example.communitracker.core.Result;
import com.example.communitracker.domain.CreditRequest;
import com.example.communitracker.domain.CreditRequestStatus;
import com.example.communitracker.domain.SolidarityEnterprise;
import com.example.communitracker.interfaces.CreateSolidarityCreditApplication;
import com.example.communitracker.interfaces.CreditRequestRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CreateSolidarityCreditUseCase implements CreateSolidarityCreditApplication {

    private static final BigDecimal MIN_AMOUNT = new BigDecimal("100.00");
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("10000.00");
    private static final int MIN_MONTHS = 1;
    private static final int MAX_MONTHS = 24;

    private final CreditRequestRepository creditRequestRepository;

    public CreateSolidarityCreditUseCase(CreditRequestRepository creditRequestRepository) {
        this.creditRequestRepository = creditRequestRepository;
    }

    @Override
    public Result<CreditRequest> execute(Command command) {
        if (command == null) {
            return new Result.Failure<>("Os dados da solicitação de crédito são obrigatórios.");
        }

        if (command.enterpriseName() == null || command.enterpriseName().isBlank()) {
            return new Result.Failure<>("O nome do empreendimento é obrigatório.");
        }

        if (command.document() == null || command.document().isBlank()) {
            return new Result.Failure<>("O documento ou registro comunitário do empreendimento é obrigatório.");
        }

        if (command.communityLocation() == null || command.communityLocation().isBlank()) {
            return new Result.Failure<>("A localização comunitária do empreendimento é obrigatória.");
        }

        if (command.segment() == null || command.segment().isBlank()) {
            return new Result.Failure<>("O segmento de atuação do empreendimento é obrigatório.");
        }

        if (command.requestedAmount() == null) {
            return new Result.Failure<>("O valor solicitado é obrigatório.");
        }

        if (command.requestedAmount().compareTo(MIN_AMOUNT) < 0) {
            return new Result.Failure<>("O valor mínimo para solicitação de crédito solidário é R$ 100,00.");
        }

        if (command.requestedAmount().compareTo(MAX_AMOUNT) > 0) {
            return new Result.Failure<>("O valor máximo para crédito solidário sem aval ampliado é R$ 10.000,00.");
        }

        if (command.installmentMonths() == null || command.installmentMonths() < MIN_MONTHS || command.installmentMonths() > MAX_MONTHS) {
            return new Result.Failure<>("O prazo de parcelamento deve ser entre 1 e 24 meses.");
        }

        if (command.purpose() == null || command.purpose().isBlank()) {
            return new Result.Failure<>("A finalidade produtiva do crédito é obrigatória.");
        }

        SolidarityEnterprise enterprise = new SolidarityEnterprise(
            command.enterpriseName().trim(),
            command.document().trim(),
            command.communityLocation().trim(),
            command.segment().trim()
        );

        CreditRequest newRequest = new CreditRequest(
            null,
            enterprise,
            command.requestedAmount(),
            command.installmentMonths(),
            BigDecimal.ZERO, // Regra inegociável do ASR: Juros fixados em 0.0%
            CreditRequestStatus.UNDER_ANALYSIS,
            LocalDateTime.now(),
            command.purpose().trim()
        );

        CreditRequest savedRequest = creditRequestRepository.save(newRequest);
        return new Result.Success<>(savedRequest);
    }
}
