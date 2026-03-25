package com.gestao.cobranca.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Parcela {

    public enum StatusParcela {
        AGUARDANDO_BOLETO, BOLETO_GERADO, PAGO, EM_ATRASO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Acordo acordo;

    private Integer numeroParcela;
    private BigDecimal valor;
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private StatusParcela status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Acordo getAcordo() { return acordo; }
    public void setAcordo(Acordo acordo) { this.acordo = acordo; }

    public Integer getNumeroParcela() { return numeroParcela; }
    public void setNumeroParcela(Integer numeroParcela) { this.numeroParcela = numeroParcela; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

    public StatusParcela getStatus() { return status; }
    public void setStatus(StatusParcela status) { this.status = status; }
}