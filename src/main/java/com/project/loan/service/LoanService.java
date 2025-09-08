package com.project.loan.service;

import com.project.loan.model.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    Loan findByid(Long id);
    Loan save(Loan loan);
    List<Loan> findAll();
    Optional<Loan> update(Long id, Loan loan);
    boolean delete(Long id);
}
