package com.gestao.cobranca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.gestao.cobranca.model.Cliente;
import com.gestao.cobranca.service.ClienteService;
import com.gestao.cobranca.service.UnidadeService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UnidadeService unidadeService;

    public ClienteController(ClienteService clienteService, UnidadeService unidadeService) {
        this.clienteService = clienteService;
        this.unidadeService = unidadeService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/listar";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam String cpf, Model model) {
        Cliente cliente = clienteService.buscarPorCpf(cpf);
        model.addAttribute("cliente", cliente);
        return "clientes/buscar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("unidades", unidadeService.listarTodas());
        return "clientes/form";
    }

    @PostMapping("/salvar")
    public String salvar(Cliente cliente) {
        clienteService.salvar(cliente);
        return "redirect:/clientes";
    }
    
    @GetMapping("/visualizar/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.buscarPorId(id));
        return "clientes/visualizar";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return "redirect:/clientes";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.buscarPorId(id));
        model.addAttribute("unidades", unidadeService.listarTodas());
        return "clientes/form";
    }
}