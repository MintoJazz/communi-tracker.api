package com.example.communitracker.controller;

import com.example.communitracker.core.Result;
import com.example.communitracker.usecase.GetAccountUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/accounts/{id}")
public class AccountDetailController {

    private final GetAccountUseCase getAccountUseCase;

    public AccountDetailController(GetAccountUseCase getAccountUseCase) {
        this.getAccountUseCase = getAccountUseCase;
    }

    @GetMapping
    public ResponseEntity<Object> getAccount(@PathVariable Long id) {
        var result = getAccountUseCase.execute(id);
        return switch (result) {
            case Result.Success<?> s -> ResponseEntity.ok(s.data());
            case Result.Failure<?> f -> ResponseEntity.status(404).body(Map.of("error", f.errorMessage()));
        };
    }
}