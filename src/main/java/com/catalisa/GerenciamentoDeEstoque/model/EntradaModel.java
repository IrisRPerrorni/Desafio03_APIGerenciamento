package com.catalisa.GerenciamentoDeEstoque.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Table(name = "entrada")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntradaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long idProduto;
    @Column(nullable = false)
    private String nomeProduto;
    @Column(nullable = false)
    private int qtdEntrada;
    @Column(nullable = false)
    private LocalDateTime entradoEm;

    public EntradaModel(Long idProduto, String nomeProduto, int qtdEntrada, LocalDateTime entradoEm) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.qtdEntrada = qtdEntrada;
        this.entradoEm = LocalDateTime.now(ZoneId.of("UTC"));
    }
}
