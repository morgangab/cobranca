package com.gestao.cobranca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gestao.cobranca.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByCpf(String cpf);

}