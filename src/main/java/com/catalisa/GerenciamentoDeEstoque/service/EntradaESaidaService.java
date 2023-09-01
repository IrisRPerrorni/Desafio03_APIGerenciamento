package com.catalisa.GerenciamentoDeEstoque.service;

import com.catalisa.GerenciamentoDeEstoque.model.EntradaModel;
import com.catalisa.GerenciamentoDeEstoque.model.SaidaModel;
import com.catalisa.GerenciamentoDeEstoque.repository.EntradaRepository;
import com.catalisa.GerenciamentoDeEstoque.repository.SaidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntradaESaidaService {

    @Autowired
    EntradaRepository entradaRepository;

    @Autowired
    SaidaRepository saidaRepository;

    public List<EntradaModel>exibirTodasEntradas(){
        return entradaRepository.findAll();
    }

    public List<SaidaModel>exibirTodasSaidas(){
        return saidaRepository.findAll();
    }




}
