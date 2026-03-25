package com.gestao.cobranca.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gestao.cobranca.model.Acordo;
import com.gestao.cobranca.model.Cliente;
import com.gestao.cobranca.model.Parcela;
import com.gestao.cobranca.service.AcordoService;
import com.gestao.cobranca.service.ClienteService;

@Controller
@RequestMapping("/acordos")
public class AcordoController {

    private final AcordoService acordoService;
    private final ClienteService clienteService;

    public AcordoController(AcordoService acordoService, ClienteService clienteService) {
        this.acordoService = acordoService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("acordos", acordoService.listarTodos());
        return "acordos/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("acordo", new Acordo());
        model.addAttribute("tipos", Acordo.Tipo.values());
        model.addAttribute("formasPagamento", Acordo.FormaPagamento.values());
        model.addAttribute("statusList", Acordo.StatusGeral.values());
        return "acordos/form";
    }

    @GetMapping("/buscarCliente")
    public String buscarCliente(@RequestParam String cpf, Model model) {
        Cliente cliente = clienteService.buscarPorCpf(cpf);
        model.addAttribute("acordo", new Acordo());
        model.addAttribute("cliente", cliente);
        model.addAttribute("clienteNaoEncontrado", cliente == null);
        model.addAttribute("tipos", Acordo.Tipo.values());
        model.addAttribute("formasPagamento", Acordo.FormaPagamento.values());
        model.addAttribute("statusList", Acordo.StatusGeral.values());
        return "acordos/form";
    }

    @PostMapping("/salvar")
    public String salvar(Acordo acordo, @RequestParam Long clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        acordo.setCliente(cliente);
        acordoService.salvar(acordo);
        return "redirect:/acordos";
    }

    @GetMapping("/visualizar/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        Acordo acordo = acordoService.buscarPorId(id);
        acordoService.verificarParcelas(acordo);
        model.addAttribute("acordo", acordoService.buscarPorId(id));
        model.addAttribute("parcelas", acordoService.listarParcelasPorAcordo(acordo));
        return "acordos/visualizar";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Acordo acordo = acordoService.buscarPorId(id);
        model.addAttribute("acordo", acordo);
        model.addAttribute("parcelas", acordoService.listarParcelasPorAcordo(acordo));
        model.addAttribute("tipos", Acordo.Tipo.values());
        model.addAttribute("formasPagamento", Acordo.FormaPagamento.values());
        model.addAttribute("statusParcela", Parcela.StatusParcela.values());
        return "acordos/editar";
    }

    @PostMapping("/editar/salvar")
    public String salvarEdicao(Acordo acordo,
            @RequestParam(required = false) List<Long> parcelaIds,
            @RequestParam(required = false) List<BigDecimal> parcelaValores,
            @RequestParam(required = false) List<LocalDate> parcelaVencimentos,
            @RequestParam(required = false) List<String> parcelaStatus) {
        Acordo acordoExistente = acordoService.buscarPorId(acordo.getId());
        acordoExistente.setTipo(acordo.getTipo());
        acordoExistente.setFormaPagamento(acordo.getFormaPagamento());
        acordoExistente.setObservacoes(acordo.getObservacoes());
        acordoService.atualizarAcordo(acordoExistente);

        if (parcelaIds != null) {
            for (int i = 0; i < parcelaIds.size(); i++) {
                Long parcelaId = parcelaIds.get(i);
                BigDecimal valor = parcelaValores != null ? parcelaValores.get(i) : null;
                LocalDate vencimento = parcelaVencimentos != null ? parcelaVencimentos.get(i) : null;
                String status = parcelaStatus != null ? parcelaStatus.get(i) : "AGUARDANDO_BOLETO";

                if (parcelaId == null || parcelaId == 0) {
                    if (valor != null && vencimento != null) {
                        Parcela novaParcela = new Parcela();
                        novaParcela.setAcordo(acordoExistente);
                        novaParcela.setNumeroParcela(i + 1);
                        novaParcela.setValor(valor);
                        novaParcela.setDataVencimento(vencimento);
                        novaParcela.setStatus(Parcela.StatusParcela.valueOf(status));
                        acordoService.salvarParcela(novaParcela);
                    }
                } else {
                    Parcela parcela = acordoService.buscarParcelaPorId(parcelaId);
                    if (parcela != null) {
                        if (valor != null) parcela.setValor(valor);
                        if (vencimento != null) parcela.setDataVencimento(vencimento);
                        parcela.setStatus(Parcela.StatusParcela.valueOf(status));
                        acordoService.salvarParcela(parcela);
                    }
                }
            }
        }

        acordoService.atualizarStatus(acordoExistente);
        return "redirect:/acordos/visualizar/" + acordoExistente.getId();
    }
    
    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id) {
        Acordo acordo = acordoService.buscarPorId(id);
        acordo.setStatusGeral(Acordo.StatusGeral.CANCELADO);
        acordoService.salvar(acordo);
        return "redirect:/acordos";
    }
    @GetMapping("/parcela/editar/{id}")
    public String editarParcela(@PathVariable Long id, Model model) {
        Parcela parcela = acordoService.buscarParcelaPorId(id);
        model.addAttribute("parcela", parcela);
        model.addAttribute("statusList", Parcela.StatusParcela.values());
        return "acordos/editarParcela";
    }

    @PostMapping("/parcela/salvar")
    public String salvarParcela(Parcela parcela) {
        Parcela parcelaExistente = acordoService.buscarParcelaPorId(parcela.getId());
        parcelaExistente.setValor(parcela.getValor());
        parcelaExistente.setDataVencimento(parcela.getDataVencimento());
        parcelaExistente.setStatus(parcela.getStatus());
        acordoService.salvarParcela(parcelaExistente);
        acordoService.atualizarStatus(parcelaExistente.getAcordo());
        return "redirect:/acordos/visualizar/" + parcelaExistente.getAcordo().getId();
    }
}