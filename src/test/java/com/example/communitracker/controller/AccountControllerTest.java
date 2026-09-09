package com.example.communitracker.controller;

import com.example.communitracker.core.Result;
import com.example.communitracker.domain.Account;
import com.example.communitracker.usecase.ListAccountsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ListAccountsUseCase listAccountsUseCase;

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
    void shouldReturnBadRequestOnFailure() throws Exception {
        when(listAccountsUseCase.execute()).thenReturn(new Result.Failure<>("Erro mockado"));

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Erro mockado"));
    }
}
