package com.gestao.cobranca.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gestao.cobranca.model.Acordo;
import com.gestao.cobranca.model.Cliente;

public interface AcordoRepository extends JpaRepository<Acordo, Long> {

    List<Acordo> findByCliente(Cliente cliente);

}