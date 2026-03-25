package com.gestao.cobranca.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gestao.cobranca.model.Parcela;
import com.gestao.cobranca.model.Acordo;

public interface ParcelaRepository extends JpaRepository<Parcela, Long> {

    List<Parcela> findByAcordo(Acordo acordo);

}