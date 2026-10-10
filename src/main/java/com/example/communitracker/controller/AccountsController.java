package com.example.communitracker.controller;

import com.example.communitracker.core.Result;
import com.example.communitracker.usecase.CreateAccountUseCase;
import com.example.communitracker.usecase.ListAccountsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private final ListAccountsUseCase listAccountsUseCase;
    private final CreateAccountUseCase createAccountUseCase;

    public AccountsController(ListAccountsUseCase listAccountsUseCase, 
                              CreateAccountUseCase createAccountUseCase) {
        this.listAccountsUseCase = listAccountsUseCase;
        this.createAccountUseCase = createAccountUseCase;
    }

    public record CreateAccountRequest(String owner, BigDecimal initialBalance) {}

    @PostMapping
    public ResponseEntity<Object> createAccount(@RequestBody CreateAccountRequest request) {
        var result = createAccountUseCase.execute(request.owner(), request.initialBalance());
        
        return switch (result) {
            case Result.Success<?> s -> ResponseEntity.status(HttpStatus.CREATED).body(s.data());
            case Result.Failure<?> f -> ResponseEntity.badRequest().body(Map.of("error", f.errorMessage()));
        };
    }

    @GetMapping
    public ResponseEntity<Object> getAccounts() {
        var result = listAccountsUseCase.execute();
        return switch (result) {
            case Result.Success<?> s -> ResponseEntity.ok(s.data());
            case Result.Failure<?> f -> ResponseEntity.badRequest().body(Map.of("error", f.errorMessage()));
        };
    }
}