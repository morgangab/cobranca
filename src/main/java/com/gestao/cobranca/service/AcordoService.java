package com.gestao.cobranca.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.gestao.cobranca.model.Acordo;
import com.gestao.cobranca.model.Cliente;
import com.gestao.cobranca.model.Parcela;
import com.gestao.cobranca.repository.AcordoRepository;
import com.gestao.cobranca.repository.ParcelaRepository;

@Service
public class AcordoService {

    private final AcordoRepository acordoRepository;
    private final ParcelaRepository parcelaRepository;

    public AcordoService(AcordoRepository acordoRepository, ParcelaRepository parcelaRepository) {
        this.acordoRepository = acordoRepository;
        this.parcelaRepository = parcelaRepository;
    }

    public List<Acordo> listarTodos() {
        return acordoRepository.findAll();
    }

    public List<Acordo> listarPorCliente(Cliente cliente) {
        return acordoRepository.findByCliente(cliente);
    }

    public Acordo buscarPorId(Long id) {
        return acordoRepository.findById(id).orElse(null);
    }

    public void salvar(Acordo acordo) {
        acordoRepository.save(acordo);

        List<Parcela> parcelas = new ArrayList<>();

        if (acordo.getValorEntrada() != null && acordo.getDataVencimentoEntrada() != null) {
            Parcela entrada = new Parcela();
            entrada.setAcordo(acordo);
            entrada.setNumeroParcela(1);
            entrada.setValor(acordo.getValorEntrada());
            entrada.setDataVencimento(acordo.getDataVencimentoEntrada());
            entrada.setStatus(Parcela.StatusParcela.AGUARDANDO_BOLETO);
            parcelas.add(entrada);
        }

        if (acordo.getNumeroParcelas() != null && acordo.getValorParcela() != null && acordo.getDataVencimentoPrimeiraParcela() != null) {
            int inicio = acordo.getValorEntrada() != null ? 2 : 1;
            for (int i = 0; i < acordo.getNumeroParcelas(); i++) {
                Parcela parcela = new Parcela();
                parcela.setAcordo(acordo);
                parcela.setNumeroParcela(inicio + i);
                parcela.setValor(acordo.getValorParcela());
                parcela.setDataVencimento(acordo.getDataVencimentoPrimeiraParcela().plusMonths(i));
                parcela.setStatus(Parcela.StatusParcela.AGUARDANDO_BOLETO);
                parcelas.add(parcela);
            }
        }

        parcelaRepository.saveAll(parcelas);
    }

    public void salvarParcela(Parcela parcela) {
        parcelaRepository.save(parcela);
    }

    public List<Parcela> listarParcelasPorAcordo(Acordo acordo) {
        return parcelaRepository.findByAcordo(acordo);
    }

    public Parcela buscarParcelaPorId(Long id) {
        return parcelaRepository.findById(id).orElse(null);
    }
}