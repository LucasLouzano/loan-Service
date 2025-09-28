package com.project.loan.service.impl;

import com.project.loan.config.BusinessException;
import com.project.loan.dto.CustomerDTO;
import com.project.loan.api.CustomerClient;
import com.project.loan.model.Loan;
import com.project.loan.repository.LoanRepository;
import com.project.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private static final double SALARIO_MINIMO = 1518.00;

    @Override
    public Loan findByid(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Loan save(Loan loan) throws BusinessException {
        validarEmprestimo(loan);
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

    public void validarEmprestimo(Loan loan) throws BusinessException {
        CustomerDTO cliente = customerClient.buscarClientePorId(loan.getCustomerId());
        if (cliente == null) {
            throw new BusinessException("Cliente não encontrado.");
        }
        // Validar idade
        int idade = Period.between(cliente.dataNascimento(), LocalDate.now()).getYears();
        if (idade < 18 || idade > 70) {
            throw new BusinessException("Cliente deve ter entre 18 e 70 anos.");
        }

        // Validar salário mínimo
        double salarioMensal = cliente.salarioMensal();
        if (salarioMensal < SALARIO_MINIMO) {
            throw new BusinessException("Salário abaixo do mínimo permitido (R$ " + SALARIO_MINIMO + ").");
        }

        // Validar valor da parcela
        BigDecimal valorEmprestimo = loan.getValor();
        int numeroParcelas = loan.getNumeroParcelas();
        BigDecimal parcelaMensal = valorEmprestimo.divide(BigDecimal.valueOf(numeroParcelas), RoundingMode.HALF_UP);

        if (parcelaMensal.doubleValue() > salarioMensal * 0.3) {
            throw new BusinessException("Parcela mensal excede 30% do salário do cliente.");
        }

        // Validar valor total do empréstimo
        if (valorEmprestimo.doubleValue() > salarioMensal * 10) {
            throw new BusinessException("Valor do empréstimo excede o limite de 10x o salário mensal.");
        }

        // Validar número de parcelas
        if (numeroParcelas > 60) {
            throw new BusinessException("Número de parcelas excede o limite de 60 meses.");
        }
    }
}