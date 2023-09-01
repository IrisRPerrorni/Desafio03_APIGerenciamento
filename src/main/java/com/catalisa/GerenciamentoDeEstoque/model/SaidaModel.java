package com.catalisa.GerenciamentoDeEstoque.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "saida")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaidaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long idProduto;
    @Column(nullable = false)
    private String nomeProduto;
    @Column(nullable = false)
    private int qtdSaida;
    @Column(nullable = false)
    private LocalDateTime saidoEm;

    public SaidaModel(Long idProduto, String nomeProduto, int qtdSaida, LocalDateTime saidoEm) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.qtdSaida = qtdSaida;
        this.saidoEm = saidoEm;
    }
}

