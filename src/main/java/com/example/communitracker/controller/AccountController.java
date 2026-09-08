package com.example.communitracker.controller;

import com.example.communitracker.core.Result;
import com.example.communitracker.usecase.ListAccountsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AccountController {

    private final ListAccountsUseCase listAccountsUseCase;

    public AccountController(ListAccountsUseCase listAccountsUseCase) {
        this.listAccountsUseCase = listAccountsUseCase;
    }

    @GetMapping("/accounts")
    public ResponseEntity<Object> getAccounts() {
        var result = listAccountsUseCase.execute();

        return switch (result) {
            case Result.Success<?> s -> ResponseEntity.ok(s.data());
            case Result.Failure<?> f -> ResponseEntity.badRequest().body(Map.of("error", f.errorMessage()));
        };
    }
}