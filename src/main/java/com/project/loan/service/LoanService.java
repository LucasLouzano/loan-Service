package com.project.loan.service;

import com.project.loan.model.Loan;
import com.project.loan.config.BusinessException;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    Loan findByid(Long id);
    Loan save(Loan loan) throws BusinessException;
    List<Loan> findAll();
    Optional<Loan> update(Long id, Loan loan);
    void delete(Long id);
}
