package com.project.loan.service.impl;

import com.project.loan.config.BusinessException;
import com.project.loan.dto.CustomerDTO;
import com.project.loan.model.CustomerClient;
import com.project.loan.model.Loan;
import com.project.loan.repository.LoanRepository;
import com.project.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private LoanRepository repository;

    @Autowired
    private CustomerClient customerClient;

    @Override
    public Loan findByid(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Loan save(Loan loan) throws BusinessException {
        CustomerDTO client = customerClient.buscarClientePorId(loan.getCustomerId());
        if (client == null) {
            throw new BusinessException("Cliente não encontrado.");
        }
        return repository.save(loan);
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Loan> update(Long id, Loan loan) {
        return repository.findById(id).map(l -> {
            l.setId(loan.getId());
            l.setValor(loan.getValor());
            l.setTaxaJuros(loan.getTaxaJuros());
            l.setNumeroParcelas(loan.getNumeroParcelas());
            l.setDataInicio(loan.getDataInicio());
            l.setDataFim(loan.getDataFim());
            return repository.save(l);
        });
    }

    @Override
    public void delete(Long id) {
        Loan loan = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        repository.delete(loan);
    }
}