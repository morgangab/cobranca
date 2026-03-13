package com.gestao.cobranca.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.gestao.cobranca.model.Unidade;
import com.gestao.cobranca.repository.UnidadeRepository;

@Service
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;

    public UnidadeService(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    public List<Unidade> listarTodas() {
        return unidadeRepository.findAll();
    }

    public void salvar(Unidade unidade) {
        unidadeRepository.save(unidade);
    }

    public void deletar(Long id) {
        unidadeRepository.deleteById(id);
    }
}