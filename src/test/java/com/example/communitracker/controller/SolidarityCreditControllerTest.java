package com.example.communitracker.controller;

import com.example.communitracker.core.Result;
import com.example.communitracker.domain.CreditRequest;
import com.example.communitracker.domain.CreditRequestStatus;
import com.example.communitracker.domain.SolidarityEnterprise;
import com.example.communitracker.interfaces.CreateSolidarityCreditApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SolidarityCreditController.class)
class SolidarityCreditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateSolidarityCreditApplication createSolidarityCreditApplication;

    @Test
    void shouldReturnCreatedWhenCreditRequestIsSuccessful() throws Exception {
        var enterprise = new SolidarityEnterprise(
            "Cooperativa Agroecológica Terra Viva",
            "01.234.567/0001-89",
            "Assentamento Bela Vista",
            "Agroecologia"
        );
        var creditRequest = new CreditRequest(
            1L,
            enterprise,
            new BigDecimal("5000.00"),
            18,
            BigDecimal.ZERO,
            CreditRequestStatus.UNDER_ANALYSIS,
            LocalDateTime.of(2026, 10, 10, 10, 0),
            "Aquisição de sistema de irrigação por gotejamento"
        );

        when(createSolidarityCreditApplication.execute(any())).thenReturn(new Result.Success<>(creditRequest));

        mockMvc.perform(post("/credit/solidarity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "enterpriseName": "Cooperativa Agroecológica Terra Viva",
                            "document": "01.234.567/0001-89",
                            "communityLocation": "Assentamento Bela Vista",
                            "segment": "Agroecologia",
                            "requestedAmount": 5000.00,
                            "installmentMonths": 18,
                            "purpose": "Aquisição de sistema de irrigação por gotejamento"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.enterprise.name").value("Cooperativa Agroecológica Terra Viva"))
                .andExpect(jsonPath("$.enterprise.document").value("01.234.567/0001-89"))
                .andExpect(jsonPath("$.enterprise.communityLocation").value("Assentamento Bela Vista"))
                .andExpect(jsonPath("$.enterprise.segment").value("Agroecologia"))
                .andExpect(jsonPath("$.requestedAmount").value(5000.00))
                .andExpect(jsonPath("$.installmentMonths").value(18))
                .andExpect(jsonPath("$.interestRate").value(0))
                .andExpect(jsonPath("$.status").value("UNDER_ANALYSIS"))
                .andExpect(jsonPath("$.purpose").value("Aquisição de sistema de irrigação por gotejamento"));
    }

    @Test
    void shouldReturnBadRequestWhenCreditRequestFails() throws Exception {
        when(createSolidarityCreditApplication.execute(any()))
                .thenReturn(new Result.Failure<>("O valor mínimo para solicitação de crédito solidário é R$ 100,00."));

        mockMvc.perform(post("/credit/solidarity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "enterpriseName": "Empreendimento Local",
                            "document": "123.456.789-00",
                            "communityLocation": "Bairro Central",
                            "segment": "Alimentação",
                            "requestedAmount": 50.00,
                            "installmentMonths": 6,
                            "purpose": "Compra de ingredientes"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("O valor mínimo para solicitação de crédito solidário é R$ 100,00."));
    }
}
