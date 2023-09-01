package com.catalisa.GerenciamentoDeEstoque.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarQtd {
    public Long id;
    private int qtdOperacao;

    public AtualizarQtd(int qtdOperacao) {
        this.qtdOperacao = qtdOperacao;
    }
}
