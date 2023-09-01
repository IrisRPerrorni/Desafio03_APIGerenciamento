package com.catalisa.GerenciamentoDeEstoque.serviceTest;


import com.catalisa.GerenciamentoDeEstoque.model.EntradaModel;

import com.catalisa.GerenciamentoDeEstoque.model.SaidaModel;
import com.catalisa.GerenciamentoDeEstoque.repository.EntradaRepository;
import com.catalisa.GerenciamentoDeEstoque.repository.SaidaRepository;
import com.catalisa.GerenciamentoDeEstoque.service.EntradaESaidaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntradaESaidaServiceTest {

    @InjectMocks
    private EntradaESaidaService entradaESaidaService;

    @Mock
    private EntradaRepository entradaRepository;

    @Mock
    private SaidaRepository saidaRepository;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testexibirTodasEntradas() {
        List<EntradaModel> entradalist = new ArrayList<>();
        LocalDateTime dataAtualEsperada = LocalDateTime.now();
        entradalist.add(new EntradaModel(1L, 1L, "teste", 10, dataAtualEsperada));
        entradalist.add(new EntradaModel(2L, 2L, "teste2", 15, dataAtualEsperada));


        when(entradaRepository.findAll()).thenReturn(entradalist);

        List<EntradaModel> resultado = entradaESaidaService.exibirTodasEntradas();

        assertEquals(entradalist.size(), resultado.size());
        assertEquals(entradalist.get(0).getId(), resultado.get(0).getId());
        assertEquals(entradalist.get(0).getIdProduto(), resultado.get(0).getIdProduto());
        assertEquals(entradalist.get(0).getNomeProduto(), resultado.get(0).getNomeProduto());
        assertEquals(entradalist.get(0).getQtdEntrada(), resultado.get(0).getQtdEntrada());
        assertEquals(entradalist.get(0).getEntradoEm(), resultado.get(0).getEntradoEm());
        assertEquals(entradalist.get(1).getId(), resultado.get(1).getId());
        assertEquals(entradalist.get(1).getIdProduto(), resultado.get(1).getIdProduto());
        assertEquals(entradalist.get(1).getNomeProduto(), resultado.get(1).getNomeProduto());
        assertEquals(entradalist.get(1).getQtdEntrada(), resultado.get(1).getQtdEntrada());
        assertEquals(entradalist.get(1).getEntradoEm(), resultado.get(1).getEntradoEm());

        // Verificar se o método findAll foi chamado no repositório
        verify(entradaRepository, times(1)).findAll();
    }

    @Test
    public void testexibirTodasSaidas() {
        List<SaidaModel> saidalist = new ArrayList<>();
        LocalDateTime dataAtualEsperada = LocalDateTime.now();
        saidalist.add(new SaidaModel(1L, 1L, "teste", 10, dataAtualEsperada));
        saidalist.add(new SaidaModel(2L, 2L, "teste2", 15, dataAtualEsperada));


        when(saidaRepository.findAll()).thenReturn(saidalist);

        List<SaidaModel> resultado = entradaESaidaService.exibirTodasSaidas();

        assertEquals(saidalist.size(), resultado.size());
        assertEquals(saidalist.get(0).getId(), resultado.get(0).getId());
        assertEquals(saidalist.get(0).getIdProduto(), resultado.get(0).getIdProduto());
        assertEquals(saidalist.get(0).getNomeProduto(), resultado.get(0).getNomeProduto());
        assertEquals(saidalist.get(0).getQtdSaida(), resultado.get(0).getQtdSaida());
        assertEquals(saidalist.get(0).getSaidoEm(), resultado.get(0).getSaidoEm());
        assertEquals(saidalist.get(1).getId(), resultado.get(1).getId());
        assertEquals(saidalist.get(1).getIdProduto(), resultado.get(1).getIdProduto());
        assertEquals(saidalist.get(1).getNomeProduto(), resultado.get(1).getNomeProduto());
        assertEquals(saidalist.get(1).getQtdSaida(), resultado.get(1).getQtdSaida());
        assertEquals(saidalist.get(1).getSaidoEm(), resultado.get(1).getSaidoEm());

        // Verificar se o método findAll foi chamado no repositório
        verify(saidaRepository, times(1)).findAll();
    }

}
