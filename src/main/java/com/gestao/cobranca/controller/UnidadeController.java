package com.gestao.cobranca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.gestao.cobranca.model.Unidade;
import com.gestao.cobranca.service.UnidadeService;

@Controller
@RequestMapping("/unidades")
public class UnidadeController {

    private final UnidadeService unidadeService;

    public UnidadeController(UnidadeService unidadeService) {
        this.unidadeService = unidadeService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("unidades", unidadeService.listarTodas());
        return "unidades/listar";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("unidade", new Unidade());
        return "unidades/form";
    }

    @PostMapping("/salvar")
    public String salvar(Unidade unidade) {
        unidadeService.salvar(unidade);
        return "redirect:/unidades";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        unidadeService.deletar(id);
        return "redirect:/unidades";
    }
}