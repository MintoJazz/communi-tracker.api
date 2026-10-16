package com.example.communitracker.usecase;

import com.example.communitracker.core.Result;
import com.example.communitracker.domain.CreditRequest;
import com.example.communitracker.domain.CreditRequestStatus;
import com.example.communitracker.domain.SolidarityEnterprise;
import com.example.communitracker.interfaces.CreateSolidarityCreditApplication;
import com.example.communitracker.interfaces.CreditRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateSolidarityCreditUseCaseTest {

    @Mock
    private CreditRequestRepository creditRequestRepository;

    @InjectMocks
    private CreateSolidarityCreditUseCase useCase;

    private CreateSolidarityCreditApplication.Command createValidCommand() {
        return new CreateSolidarityCreditApplication.Command(
            "Coletivo Costura Solidária",
            "12.345.678/0001-90",
            "Bairro Periférico Sul",
            "Confecção Têxtil Comunitária",
            new BigDecimal("3500.00"),
            12,
            "Aquisição de maquinário de costura reta industrial"
        );
    }

    @Test
    void shouldReturnSuccessWhenCreatingValidCreditRequest() {
        var command = createValidCommand();
        var mockSaved = new CreditRequest(
            1L,
            new SolidarityEnterprise(
                command.enterpriseName(),
                command.document(),
                command.communityLocation(),
                command.segment()
            ),
            command.requestedAmount(),
            command.installmentMonths(),
            BigDecimal.ZERO,
            CreditRequestStatus.UNDER_ANALYSIS,
            LocalDateTime.now(),
            command.purpose()
        );

        when(creditRequestRepository.save(any(CreditRequest.class))).thenReturn(mockSaved);

        Result<CreditRequest> result = useCase.execute(command);

        assertInstanceOf(Result.Success.class, result);
        var success = (Result.Success<CreditRequest>) result;
        assertEquals(1L, success.data().id());
        assertEquals("Coletivo Costura Solidária", success.data().enterprise().name());
        assertEquals("12.345.678/0001-90", success.data().enterprise().document());
        assertEquals("Bairro Periférico Sul", success.data().enterprise().communityLocation());
        assertEquals("Confecção Têxtil Comunitária", success.data().enterprise().segment());
        assertEquals(new BigDecimal("3500.00"), success.data().requestedAmount());
        assertEquals(12, success.data().installmentMonths());
        assertEquals(BigDecimal.ZERO, success.data().interestRate()); // ASR: Taxa zero
        assertEquals(CreditRequestStatus.UNDER_ANALYSIS, success.data().status());
        verify(creditRequestRepository).save(any(CreditRequest.class));
    }

    @Test
    void shouldReturnFailureWhenCommandIsNull() {
        Result<CreditRequest> result = useCase.execute(null);

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<CreditRequest>) result;
        assertEquals("Os dados da solicitação de crédito são obrigatórios.", failure.errorMessage());
        verifyNoInteractions(creditRequestRepository);
    }

    @Test
    void shouldReturnFailureWhenEnterpriseNameIsBlank() {
        var command = new CreateSolidarityCreditApplication.Command(
            "   ",
            "12.345.678/0001-90",
            "Comunidade da Paz",
            "Artesanato",
            new BigDecimal("1000.00"),
            6,
            "Compra de matéria-prima"
        );

        Result<CreditRequest> result = useCase.execute(command);

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<CreditRequest>) result;
        assertEquals("O nome do empreendimento é obrigatório.", failure.errorMessage());
        verifyNoInteractions(creditRequestRepository);
    }

    @Test
    void shouldReturnFailureWhenDocumentIsBlank() {
        var command = new CreateSolidarityCreditApplication.Command(
            "Padaria Comunitária",
            "",
            "Comunidade da Paz",
            "Alimentação",
            new BigDecimal("1000.00"),
            6,
            "Compra de farinha"
        );

        Result<CreditRequest> result = useCase.execute(command);

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<CreditRequest>) result;
        assertEquals("O documento ou registro comunitário do empreendimento é obrigatório.", failure.errorMessage());
        verifyNoInteractions(creditRequestRepository);
    }

    @Test
    void shouldReturnFailureWhenCommunityLocationIsBlank() {
        var command = new CreateSolidarityCreditApplication.Command(
            "Padaria Comunitária",
            "123.456.789-00",
            "   ",
            "Alimentação",
            new BigDecimal("1000.00"),
            6,
            "Compra de farinha"
        );

        Result<CreditRequest> result = useCase.execute(command);

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<CreditRequest>) result;
        assertEquals("A localização comunitária do empreendimento é obrigatória.", failure.errorMessage());
        verifyNoInteractions(creditRequestRepository);
    }

    @Test
    void shouldReturnFailureWhenSegmentIsBlank() {
        var command = new CreateSolidarityCreditApplication.Command(
            "Padaria Comunitária",
            "123.456.789-00",
            "Comunidade da Paz",
            null,
            new BigDecimal("1000.00"),
            6,
            "Compra de farinha"
        );

        Result<CreditRequest> result = useCase.execute(command);

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<CreditRequest>) result;
        assertEquals("O segmento de atuação do empreendimento é obrigatório.", failure.errorMessage());
        verifyNoInteractions(creditRequestRepository);
    }

    @Test
    void shouldReturnFailureWhenRequestedAmountIsNull() {
        var command = new CreateSolidarityCreditApplication.Command(
            "Horta Coletiva",
            "123.456.789-00",
            "Vila Esperança",
            "Agroecologia",
            null,
            6,
            "Adubo orgânico"
        );

        Result<CreditRequest> result = useCase.execute(command);

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<CreditRequest>) result;
        assertEquals("O valor solicitado é obrigatório.", failure.errorMessage());
        verifyNoInteractions(creditRequestRepository);
    }

    @Test
    void shouldReturnFailureWhenRequestedAmountIsLessThanMinimum() {
        var command = new CreateSolidarityCreditApplication.Command(
            "Horta Coletiva",
            "123.456.789-00",
            "Vila Esperança",
            "Agroecologia",
            new BigDecimal("99.99"),
            6,
            "Adubo orgânico"
        );

        Result<CreditRequest> result = useCase.execute(command);

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<CreditRequest>) result;
        assertEquals("O valor mínimo para solicitação de crédito solidário é R$ 100,00.", failure.errorMessage());
        verifyNoInteractions(creditRequestRepository);
    }

    @Test
    void shouldReturnFailureWhenRequestedAmountExceedsMaximum() {
        var command = new CreateSolidarityCreditApplication.Command(
            "Horta Coletiva",
            "123.456.789-00",
            "Vila Esperança",
            "Agroecologia",
            new BigDecimal("10000.01"),
            12,
            "Expansão de estufa"
        );

        Result<CreditRequest> result = useCase.execute(command);

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<CreditRequest>) result;
        assertEquals("O valor máximo para crédito solidário sem aval ampliado é R$ 10.000,00.", failure.errorMessage());
        verifyNoInteractions(creditRequestRepository);
    }

    @Test
    void shouldReturnFailureWhenInstallmentMonthsIsLessThanOne() {
        var command = new CreateSolidarityCreditApplication.Command(
            "Marcenaria Popular",
            "123.456.789-00",
            "Vila Esperança",
            "Marcenaria",
            new BigDecimal("2000.00"),
            0,
            "Madeira de reflorestamento"
        );

        Result<CreditRequest> result = useCase.execute(command);

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<CreditRequest>) result;
        assertEquals("O prazo de parcelamento deve ser entre 1 e 24 meses.", failure.errorMessage());
        verifyNoInteractions(creditRequestRepository);
    }

    @Test
    void shouldReturnFailureWhenInstallmentMonthsExceedsTwentyFour() {
        var command = new CreateSolidarityCreditApplication.Command(
            "Marcenaria Popular",
            "123.456.789-00",
            "Vila Esperança",
            "Marcenaria",
            new BigDecimal("2000.00"),
            25,
            "Madeira de reflorestamento"
        );

        Result<CreditRequest> result = useCase.execute(command);

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<CreditRequest>) result;
        assertEquals("O prazo de parcelamento deve ser entre 1 e 24 meses.", failure.errorMessage());
        verifyNoInteractions(creditRequestRepository);
    }

    @Test
    void shouldReturnFailureWhenPurposeIsBlank() {
        var command = new CreateSolidarityCreditApplication.Command(
            "Marcenaria Popular",
            "123.456.789-00",
            "Vila Esperança",
            "Marcenaria",
            new BigDecimal("2000.00"),
            10,
            "   "
        );

        Result<CreditRequest> result = useCase.execute(command);

        assertInstanceOf(Result.Failure.class, result);
        var failure = (Result.Failure<CreditRequest>) result;
        assertEquals("A finalidade produtiva do crédito é obrigatória.", failure.errorMessage());
        verifyNoInteractions(creditRequestRepository);
    }
}
