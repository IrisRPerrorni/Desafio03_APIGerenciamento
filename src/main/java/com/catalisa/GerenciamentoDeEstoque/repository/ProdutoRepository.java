package com.catalisa.GerenciamentoDeEstoque.repository;

import com.catalisa.GerenciamentoDeEstoque.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoModel,Long> {


}
