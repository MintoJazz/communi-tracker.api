package com.example.communitracker.controller;

import com.example.communitracker.core.Result;
import com.example.communitracker.domain.Account;
import com.example.communitracker.usecase.GetAccountUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountDetailController.class)
class AccountDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetAccountUseCase getAccountUseCase;

    @Test
    void shouldReturnAccountOnSuccess() throws Exception {
        var account = new Account(1L, "Minto", BigDecimal.valueOf(100));
        when(getAccountUseCase.execute(1L)).thenReturn(new Result.Success<>(account));

        mockMvc.perform(get("/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.owner").value("Minto"))
                .andExpect(jsonPath("$.balance").value(100));
    }

    @Test
    void shouldReturnNotFoundOnFailure() throws Exception {
        when(getAccountUseCase.execute(1L)).thenReturn(new Result.Failure<>("Conta não encontrada."));

        mockMvc.perform(get("/accounts/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Conta não encontrada."));
    }
}
