package com.project.loan.controller;

import com.project.loan.model.Loan;
import com.project.loan.config.BusinessException;
import com.project.loan.service.LoanService;
import com.project.loan.service.impl.LoanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/loans")
public class LoanController {
    @Autowired
    private LoanService service;

    @GetMapping
    public ResponseEntity<List<Loan>> getLoan() {
        List<Loan> loanList = service.findAll();
        if (loanList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(loanList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> searchallById(@PathVariable Long id) {
        Loan loan = service.findByid(id);
        if (loan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(loan);
    }

    @PostMapping()
    public ResponseEntity<Loan> saveLoan(@RequestBody Loan loan) throws BusinessException {
        Loan loan1 = service.save(loan);
        if (loan1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(loan1);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Loan> updateCustomer(@PathVariable Long id, @RequestBody Loan loan) {
        Optional<Loan> UpdateLoan = service.update(id, loan);
        return UpdateLoan
                .map(l -> ResponseEntity.ok().body(l))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}