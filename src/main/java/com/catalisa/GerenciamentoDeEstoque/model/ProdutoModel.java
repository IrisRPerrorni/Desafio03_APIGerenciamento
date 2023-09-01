package com.catalisa.GerenciamentoDeEstoque.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "produtos")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String descricao;
    @Column(nullable = false)
    private double preco;
    @Column(nullable = false)
    private int qtd;
    @Column
    private int qtdOperacao;
}


