package com.example.communitracker.controller;

import com.example.communitracker.core.Result;
import com.example.communitracker.domain.Account;
import com.example.communitracker.usecase.CreateAccountUseCase;
import com.example.communitracker.usecase.ListAccountsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountsController.class)
class AccountsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ListAccountsUseCase listAccountsUseCase;

    @MockitoBean
    private CreateAccountUseCase createAccountUseCase;

    @Test
    void shouldReturnAccountsOnSuccess() throws Exception {
        var account = new Account(1L, "Minto", BigDecimal.valueOf(100));
        when(listAccountsUseCase.execute()).thenReturn(new Result.Success<>(List.of(account)));

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].owner").value("Minto"))
                .andExpect(jsonPath("$[0].balance").value(100));
    }

    @Test
    void shouldReturnBadRequestWhenListAccountsFails() throws Exception {
        when(listAccountsUseCase.execute()).thenReturn(new Result.Failure<>("Erro ao consultar o banco de dados comunitário."));

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Erro ao consultar o banco de dados comunitário."));
    }

    @Test
    void shouldCreateAccountOnSuccess() throws Exception {
        var account = new Account(1L, "Fundo Solidário", new BigDecimal("500.00"));
        when(createAccountUseCase.execute(eq("Fundo Solidário"), any(BigDecimal.class)))
                .thenReturn(new Result.Success<>(account));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "owner": "Fundo Solidário",
                            "initialBalance": 500.00
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.owner").value("Fundo Solidário"))
                .andExpect(jsonPath("$.balance").value(500.00));
    }

    @Test
    void shouldReturnBadRequestWhenCreateAccountFails() throws Exception {
        when(createAccountUseCase.execute(eq(""), any()))
                .thenReturn(new Result.Failure<>("O nome do titular é obrigatório."));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "owner": "",
                            "initialBalance": 100.00
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("O nome do titular é obrigatório."));
    }
}
