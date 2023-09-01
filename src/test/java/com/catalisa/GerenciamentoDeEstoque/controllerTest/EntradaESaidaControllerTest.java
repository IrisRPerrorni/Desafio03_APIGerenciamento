package com.catalisa.GerenciamentoDeEstoque.controllerTest;

import com.catalisa.GerenciamentoDeEstoque.controller.EntradaESaidaController;

import com.catalisa.GerenciamentoDeEstoque.model.EntradaModel;

import com.catalisa.GerenciamentoDeEstoque.model.SaidaModel;
import com.catalisa.GerenciamentoDeEstoque.service.EntradaESaidaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EntradaESaidaController.class)
public class EntradaESaidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EntradaESaidaService entradaESaidaService;

    @Test
    public void testExibeHistoricoEntrada() throws Exception {
        LocalDateTime dataAtualEsperada = LocalDateTime.now();
        EntradaModel entrada1 = new EntradaModel(1L, 1L, "teste", 10, dataAtualEsperada);
        EntradaModel entrada2 = new EntradaModel(2L, 2L, "teste2", 15, dataAtualEsperada);

        when(entradaESaidaService.exibirTodasEntradas()).thenReturn(List.of(entrada1, entrada2));

        mockMvc.perform(get("/api/historico/entrada"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].idProduto").value(1L))
                .andExpect(jsonPath("$[0].nomeProduto").value("teste"))
                .andExpect(jsonPath("$[0].qtdEntrada").value(10))
                .andExpect(jsonPath("$[0].entradoEm").isNotEmpty()) // Não verifique diretamente o LocalDateTime
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].idProduto").value(2L))
                .andExpect(jsonPath("$[1].nomeProduto").value("teste2"))
                .andExpect(jsonPath("$[1].qtdEntrada").value(15))
                .andExpect(jsonPath("$[1].entradoEm").isNotEmpty()); // Não verifique diretamente o LocalDateTime

        verify(entradaESaidaService, times(1)).exibirTodasEntradas();
    }

    @Test
    public void exibirHistoricoEntradaVazioTest() throws Exception {
        List<EntradaModel> entrada = Collections.emptyList();

        when(entradaESaidaService.exibirTodasEntradas()).thenReturn(entrada);

        mockMvc.perform(get("/api/historico/entrada"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(entrada)));
    }

    @Test
    public void testExibeHistoricoSaida() throws Exception {
        LocalDateTime dataAtualEsperada = LocalDateTime.now();
        SaidaModel saida1 = new SaidaModel(1L, 1L, "teste", 10, dataAtualEsperada);
        SaidaModel saida2 = new SaidaModel(2L, 2L, "teste2", 15, dataAtualEsperada);

        when(entradaESaidaService.exibirTodasSaidas()).thenReturn(List.of(saida1, saida2));

        mockMvc.perform(get("/api/historico/saida"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].idProduto").value(1L))
                .andExpect(jsonPath("$[0].nomeProduto").value("teste"))
                .andExpect(jsonPath("$[0].qtdSaida").value(10))
                .andExpect(jsonPath("$[0].saidoEm").isNotEmpty())
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].idProduto").value(2L))
                .andExpect(jsonPath("$[1].nomeProduto").value("teste2"))
                .andExpect(jsonPath("$[1].qtdSaida").value(15))
                .andExpect(jsonPath("$[1].saidoEm").isNotEmpty());

        verify(entradaESaidaService, times(1)).exibirTodasSaidas();


    }

    @Test
    public void exibirHistoricoSaidaVaziaTest() throws Exception {
        List<SaidaModel> saida = Collections.emptyList();

        when(entradaESaidaService.exibirTodasSaidas()).thenReturn(saida);

        mockMvc.perform(get("/api/historico/saida"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(saida)));
    }


}







