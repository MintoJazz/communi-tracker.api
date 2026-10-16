package com.example.communitracker.interfaces;

import com.example.communitracker.domain.CreditRequest;

import java.util.List;
import java.util.Optional;

public interface CreditRequestRepository {
    CreditRequest save(CreditRequest creditRequest);
    Optional<CreditRequest> findById(Long id);
    List<CreditRequest> findAll();
}
