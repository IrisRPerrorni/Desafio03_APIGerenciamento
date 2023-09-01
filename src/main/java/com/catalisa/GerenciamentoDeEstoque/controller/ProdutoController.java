package com.catalisa.GerenciamentoDeEstoque.controller;

import com.catalisa.GerenciamentoDeEstoque.dto.AtualizarQtd;
import com.catalisa.GerenciamentoDeEstoque.dto.ListarProduto;
import com.catalisa.GerenciamentoDeEstoque.dto.ProdutoDTO;
import com.catalisa.GerenciamentoDeEstoque.model.ProdutoModel;
import com.catalisa.GerenciamentoDeEstoque.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "produtos")
public class ProdutoController {

    @Autowired
    ProdutoService produtoService;

    // testado com as condições cadastro ok , cadastro error;
    @Operation(summary = "Cadastro novo produto", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping
    public ResponseEntity<Object> cadastrarNovoProduto(@Valid @RequestBody ProdutoDTO criarProduto) {
        try {
            ProdutoDTO novoProduto = produtoService.cadastrarProduto(criarProduto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar a conta:" + e.getMessage());
        }
    }

    // testado produto encontrado e não encontrado ;

    @Operation(summary = "Busca produto pelo ID", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> listarProdutoEspecifico(@PathVariable(value = "id") Long id) {
        Optional<ProdutoModel> produtoModel = produtoService.exibirEspecifico(id);
        if (!produtoModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(produtoModel.get());
    }

    // testado com lista preenchida , lista vazia;
    @Operation(summary = "Busca todos os produtos", method = "GET")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"))
    @GetMapping
    public ResponseEntity<List<ListarProduto>> exibirTodosProdutos() {
        List<ListarProduto> produtos = produtoService.exibirProdutos();
        return ResponseEntity.ok(produtos);
    }

    //errado teste
    @Operation(summary = "Adiciona no historico estoque a adição do produto", method = "PUT")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Estoque atualizado"))
    @PutMapping(path = "/entrada/{id}")
    public ResponseEntity<ProdutoModel> AlterandoEntradaEstoque(@PathVariable Long id,
                                                                @RequestBody AtualizarQtd atualizarQtd) {
        ProdutoModel produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(atualizarQtd, produtoModel);
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.entradaProduto(id, atualizarQtd));
    }

    //testado
    @Operation(summary = "Adiciona no historico estoque a retirada do produto", method = "PUT")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Estoque atualizado"))
    @PutMapping(path = "/saida/{id}")
    public ResponseEntity<ProdutoModel> alterandoSaidaEstoque(@PathVariable Long id,
                                                              @RequestBody AtualizarQtd atualizarQtd) {
        ProdutoModel produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(atualizarQtd, produtoModel);
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.saidaProduto(id, atualizarQtd));
    }

    //testado
    @Operation(summary = "Altera as informações do produto", method = "PUT")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Produto atualizado"))
    @PutMapping(path = "/alterar/{id}")
    public ResponseEntity<ProdutoModel> alterarProduto(@PathVariable Long id,
                                                       @RequestBody ProdutoDTO produtoDTO) {
        ProdutoModel produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoDTO, produtoModel);
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.atualizarProduto(id, produtoDTO));

    }
    //testado com produto existente e não existente
    @Operation(summary = "Delecao de produto pelo ID", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deletarProduto(@PathVariable(value = "id") Long id) {
        Optional<ProdutoModel> produtoModelOptionalOptional = produtoService.exibirEspecifico(id);
        if (!produtoModelOptionalOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");

        }
        produtoService.deletar(produtoModelOptionalOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletada com sucesso");
    }
}
