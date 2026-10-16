package com.example.communitracker.controller;

import com.example.communitracker.core.Result;
import com.example.communitracker.interfaces.CreateSolidarityCreditApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/credit/solidarity")
public class SolidarityCreditController {

    private final CreateSolidarityCreditApplication createSolidarityCreditApplication;

    public SolidarityCreditController(CreateSolidarityCreditApplication createSolidarityCreditApplication) {
        this.createSolidarityCreditApplication = createSolidarityCreditApplication;
    }

    public record CreateSolidarityCreditHttpRequest(
        String enterpriseName,
        String document,
        String communityLocation,
        String segment,
        BigDecimal requestedAmount,
        Integer installmentMonths,
        String purpose
    ) {}

    @PostMapping
    public ResponseEntity<Object> requestCredit(@RequestBody CreateSolidarityCreditHttpRequest request) {
        var command = new CreateSolidarityCreditApplication.Command(
            request.enterpriseName(),
            request.document(),
            request.communityLocation(),
            request.segment(),
            request.requestedAmount(),
            request.installmentMonths(),
            request.purpose()
        );

        var result = createSolidarityCreditApplication.execute(command);

        return switch (result) {
            case Result.Success<?> s -> ResponseEntity.status(HttpStatus.CREATED).body(s.data());
            case Result.Failure<?> f -> ResponseEntity.badRequest().body(Map.of("error", f.errorMessage()));
        };
    }
}
