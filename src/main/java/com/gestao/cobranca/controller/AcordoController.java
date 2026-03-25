package com.gestao.cobranca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.gestao.cobranca.model.Acordo;
import com.gestao.cobranca.model.Cliente;
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
        model.addAttribute("acordo", acordo);
        model.addAttribute("parcelas", acordoService.listarParcelasPorAcordo(acordo));
        return "acordos/visualizar";
    }
}