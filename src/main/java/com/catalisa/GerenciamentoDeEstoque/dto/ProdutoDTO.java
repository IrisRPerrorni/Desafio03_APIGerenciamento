package com.catalisa.GerenciamentoDeEstoque.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {
    @NotBlank(message = "O campo nome é obrigatório")
    private String nome;
    @NotBlank(message = "O campo descrição é obrigatório")
    private String descricao;
    @NotNull(message = "O campo preço é obrigatório")
    private double preco;
    @NotNull(message = "O campo qtd é obrigatório")
    private int qtd;

}
