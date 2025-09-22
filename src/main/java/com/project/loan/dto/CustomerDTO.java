package com.project.loan.dto;
import java.time.LocalDate;
public record CustomerDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        LocalDate dataNascimento,
        String endereco,
        String cidade,
        String estado,
        String cep,
        String cargo,
        double salarioMensal
) {}