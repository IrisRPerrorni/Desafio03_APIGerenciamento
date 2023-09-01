package com.catalisa.GerenciamentoDeEstoque.repository;

import com.catalisa.GerenciamentoDeEstoque.model.EntradaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntradaRepository extends JpaRepository<EntradaModel,Long> {
}
