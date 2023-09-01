package com.catalisa.GerenciamentoDeEstoque.controller;

import com.catalisa.GerenciamentoDeEstoque.model.EntradaModel;
import com.catalisa.GerenciamentoDeEstoque.model.SaidaModel;
import com.catalisa.GerenciamentoDeEstoque.service.EntradaESaidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "historico")
public class EntradaESaidaController {

    @Autowired
    EntradaESaidaService entradaESaidaService;

    @Operation(summary = "Lista o histórico de entrada", method = "GET")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"))
    @GetMapping(path = "/api/historico/entrada")   // caminho da requisição
    public List<EntradaModel> exibeHistoricoEntrada(){
        return entradaESaidaService.exibirTodasEntradas();
    }
    @Operation(summary = "Lista o histórico de saida", method = "GET")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"))
    @GetMapping(path = "/api/historico/saida")   // caminho da requisição
    public List<SaidaModel> exibeHistoricoSaida(){

        return entradaESaidaService.exibirTodasSaidas();
    }


}
