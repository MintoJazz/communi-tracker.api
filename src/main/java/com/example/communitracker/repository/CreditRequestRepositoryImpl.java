package com.example.communitracker.repository;

import com.example.communitracker.domain.CreditRequest;
import com.example.communitracker.interfaces.CreditRequestRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CreditRequestRepositoryImpl implements CreditRequestRepository {

    private final CreditRequestDataStore db;

    public CreditRequestRepositoryImpl(CreditRequestDataStore db) {
        this.db = db;
    }

    @Override
    public synchronized CreditRequest save(CreditRequest creditRequest) {
        Long newId = (long) (db.creditRequests.size() + 1);
        CreditRequest saved = new CreditRequest(
            newId,
            creditRequest.enterprise(),
            creditRequest.requestedAmount(),
            creditRequest.installmentMonths(),
            creditRequest.interestRate(),
            creditRequest.status(),
            creditRequest.requestedAt(),
            creditRequest.purpose()
        );
        db.creditRequests.add(saved);
        return saved;
    }

    @Override
    public Optional<CreditRequest> findById(Long id) {
        return db.creditRequests.stream()
            .filter(r -> r.id().equals(id))
            .findFirst();
    }

    @Override
    public List<CreditRequest> findAll() {
        return new ArrayList<>(db.creditRequests);
    }
}
