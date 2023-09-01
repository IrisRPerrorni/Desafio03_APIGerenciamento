package com.catalisa.GerenciamentoDeEstoque.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListarProduto {

    private Long id;
    private String nome;
    private String descricao;
    private double preco;
    private int qtd;
}
