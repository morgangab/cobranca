package com.gestao.cobranca.service;

import java.time.LocalDate;
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
        Acordo acordoSalvo = acordoRepository.save(acordo);

        List<Parcela> parcelas = new ArrayList<>();

        if (acordoSalvo.getValorEntrada() != null && acordoSalvo.getDataVencimentoEntrada() != null) {
            Parcela entrada = new Parcela();
            entrada.setAcordo(acordoSalvo);
            entrada.setNumeroParcela(1);
            entrada.setValor(acordoSalvo.getValorEntrada());
            entrada.setDataVencimento(acordoSalvo.getDataVencimentoEntrada());
            entrada.setStatus(Parcela.StatusParcela.AGUARDANDO_BOLETO);
            parcelas.add(entrada);
        }

        if (acordoSalvo.getNumeroParcelas() != null && acordoSalvo.getValorParcela() != null && acordoSalvo.getDataVencimentoPrimeiraParcela() != null) {
            int inicio = acordoSalvo.getValorEntrada() != null ? 2 : 1;
            for (int i = 0; i < acordoSalvo.getNumeroParcelas(); i++) {
                Parcela parcela = new Parcela();
                parcela.setAcordo(acordoSalvo);
                parcela.setNumeroParcela(inicio + i);
                parcela.setValor(acordoSalvo.getValorParcela());
                parcela.setDataVencimento(acordoSalvo.getDataVencimentoPrimeiraParcela().plusMonths(i));
                parcela.setStatus(Parcela.StatusParcela.AGUARDANDO_BOLETO);
                parcelas.add(parcela);
            }
        }

        if (!parcelas.isEmpty()) {
            parcelaRepository.saveAll(parcelas);
        }
    }

    public void salvarParcela(Parcela parcela) {
        parcelaRepository.save(parcela);
    }

    public List<Parcela> listarParcelasPorAcordo(Acordo acordo) {
        return parcelaRepository.findByAcordoOrderByDataVencimentoAsc(acordo);
    }

    public Parcela buscarParcelaPorId(Long id) {
        return parcelaRepository.findById(id).orElse(null);
    }
    
    public void atualizarStatus(Acordo acordo) {
        List<Parcela> parcelas = parcelaRepository.findByAcordoOrderByDataVencimentoAsc(acordo);

        if (acordo.getStatusGeral() == Acordo.StatusGeral.CANCELADO) {
            return;
        }

        boolean todasPagas = parcelas.stream()
            .allMatch(p -> p.getStatus() == Parcela.StatusParcela.PAGO);

        boolean algumAtraso = parcelas.stream()
            .anyMatch(p -> p.getStatus() == Parcela.StatusParcela.EM_ATRASO);

        if (todasPagas) {
            acordo.setStatusGeral(Acordo.StatusGeral.QUITADO);
        } else if (algumAtraso) {
            acordo.setStatusGeral(Acordo.StatusGeral.EM_ATRASO);
        } else {
            acordo.setStatusGeral(Acordo.StatusGeral.EM_DIA);
        }

        acordoRepository.save(acordo);
    }
    
    public void atualizarAcordo(Acordo acordo) {
        acordoRepository.save(acordo);
    }
    
    public void verificarParcelas(Acordo acordo) {
        if (acordo.getStatusGeral() == Acordo.StatusGeral.CANCELADO ||
            acordo.getStatusGeral() == Acordo.StatusGeral.QUITADO) {
            return;
        }

        List<Parcela> parcelas = parcelaRepository.findByAcordoOrderByDataVencimentoAsc(acordo);
        LocalDate hoje = LocalDate.now();

        for (Parcela parcela : parcelas) {
            if (parcela.getStatus() == Parcela.StatusParcela.AGUARDANDO_BOLETO ||
                parcela.getStatus() == Parcela.StatusParcela.BOLETO_GERADO) {
                if (parcela.getDataVencimento() != null && parcela.getDataVencimento().isBefore(hoje)) {
                    parcela.setStatus(Parcela.StatusParcela.EM_ATRASO);
                    parcelaRepository.save(parcela);
                }
            }
        }

        atualizarStatus(acordo);
    }
}