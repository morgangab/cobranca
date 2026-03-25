package com.gestao.cobranca.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Acordo {

    public enum Tipo {
        PARCELAS_VENCIDAS, QUITACAO, CANCELAMENTO
    }

    public enum FormaPagamento {
        BOLETO, CARTAO, PIX, A_VISTA
    }

    public enum StatusGeral {
        EM_DIA, EM_ATRASO, QUITADO, CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    private BigDecimal valorEntrada;
    private LocalDate dataVencimentoEntrada;

    private Integer numeroParcelas;
    private BigDecimal valorParcela;
    private LocalDate dataVencimentoPrimeiraParcela;

    private String observacoes;

    @Enumerated(EnumType.STRING)
    private StatusGeral statusGeral;

    @OneToMany(mappedBy = "acordo", cascade = CascadeType.ALL)
    private List<Parcela> parcelas;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public BigDecimal getValorEntrada() { return valorEntrada; }
    public void setValorEntrada(BigDecimal valorEntrada) { this.valorEntrada = valorEntrada; }

    public LocalDate getDataVencimentoEntrada() { return dataVencimentoEntrada; }
    public void setDataVencimentoEntrada(LocalDate dataVencimentoEntrada) { this.dataVencimentoEntrada = dataVencimentoEntrada; }

    public Integer getNumeroParcelas() { return numeroParcelas; }
    public void setNumeroParcelas(Integer numeroParcelas) { this.numeroParcelas = numeroParcelas; }

    public BigDecimal getValorParcela() { return valorParcela; }
    public void setValorParcela(BigDecimal valorParcela) { this.valorParcela = valorParcela; }

    public LocalDate getDataVencimentoPrimeiraParcela() { return dataVencimentoPrimeiraParcela; }
    public void setDataVencimentoPrimeiraParcela(LocalDate dataVencimentoPrimeiraParcela) { this.dataVencimentoPrimeiraParcela = dataVencimentoPrimeiraParcela; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public StatusGeral getStatusGeral() { return statusGeral; }
    public void setStatusGeral(StatusGeral statusGeral) { this.statusGeral = statusGeral; }

    public List<Parcela> getParcelas() { return parcelas; }
    public void setParcelas(List<Parcela> parcelas) { this.parcelas = parcelas; }
}