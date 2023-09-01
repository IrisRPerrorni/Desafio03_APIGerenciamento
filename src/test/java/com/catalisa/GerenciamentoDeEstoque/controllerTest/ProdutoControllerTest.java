package com.catalisa.GerenciamentoDeEstoque.controllerTest;

import com.catalisa.GerenciamentoDeEstoque.controller.ProdutoController;
import com.catalisa.GerenciamentoDeEstoque.dto.AtualizarQtd;
import com.catalisa.GerenciamentoDeEstoque.dto.ListarProduto;
import com.catalisa.GerenciamentoDeEstoque.dto.ProdutoDTO;
import com.catalisa.GerenciamentoDeEstoque.model.ProdutoModel;
import com.catalisa.GerenciamentoDeEstoque.service.ProdutoService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProdutoController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProdutoService produtoService;

    @Test
    public void cadastrarNovoProdutoTest() throws Exception {
        ProdutoDTO produto1 = new ProdutoDTO("iphone", "iphone 13 preto", 5000, 3);
        when(produtoService.cadastrarProduto(any(ProdutoDTO.class))).thenReturn(produto1);
        mockMvc.perform(post("/api/produtos").contentType("application/json")
                        .content(objectMapper.writeValueAsString(produto1)))
                .andExpect(status().isCreated()) // Verifica o status 201 Created
                .andExpect(jsonPath("$.nome").value("iphone")) // Verifica o campo "nome"
                .andExpect(jsonPath("$.descricao").value("iphone 13 preto")) // Verifica o campo "descricao"
                .andExpect(jsonPath("$.preco").value(5000)) // Verifica o campo "preco"
                .andExpect(jsonPath("$.qtd").value(3)) // Verifica o campo "quantidade"
                .andExpect(content().json(objectMapper.writeValueAsString(produto1)));

        verify(produtoService).cadastrarProduto(any(ProdutoDTO.class)); // Verificar se o método do serviço foi chamado corretamente

    }

    @Test
    public void erroCadastroContaTest() throws Exception {
        ProdutoDTO cadastroInvalido = new ProdutoDTO("", "", 0, 0);
        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastroInvalido)))
                .andExpect(status().isBadRequest());

        // Verificar que o método do serviço não foi chamado (validação falhou)
        verify(produtoService, never()).cadastrarProduto(any(ProdutoDTO.class));
    }

    @Test
    public void exibirTodosProdutosTest() throws Exception {

        ListarProduto produto1 = new ListarProduto(1L,"iphone", "iphone 13 preto", 5000, 3);
        ListarProduto produto2 = new ListarProduto(2L,"monitor", "monitor preto LG", 1000, 4);

        Mockito.when(produtoService.exibirProdutos()).thenReturn(List.of(produto1,produto2));

        mockMvc.perform(get("/api/produtos")).andExpect(status().isOk()).
                andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("iphone"))
                .andExpect(jsonPath("$[0].descricao").value("iphone 13 preto"))
                .andExpect(jsonPath("$[0].preco").value(5000))
                .andExpect(jsonPath("$[0].qtd").value(3))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nome").value("monitor"))
                .andExpect(jsonPath("$[1].descricao").value("monitor preto LG"))
                .andExpect(jsonPath("$[1].preco").value(1000))
                .andExpect(jsonPath("$[1].qtd").value(4));

        Mockito.verify(produtoService,times(1)).exibirProdutos();
    }

    @Test
    public void exibirListaVaziaTest() throws Exception {
        List<ListarProduto> produtos = Collections.emptyList();

        when(produtoService.exibirProdutos()).thenReturn(produtos);

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(produtos)));
    }

    @Test
    public void listarProdutoEspecificoTest() throws Exception {
        Long id = 1L;
        ProdutoModel produtoModel = new ProdutoModel(1L,"iphone", "iphone 13 preto", 5000, 3,0);
        Optional<ProdutoModel> produtoModelOptional = Optional.of(produtoModel);
        when(produtoService.exibirEspecifico(id)).thenReturn(produtoModelOptional);
        mockMvc.perform(get("/api/produtos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("iphone"))
                .andExpect(jsonPath("$.descricao").value("iphone 13 preto"))
                .andExpect(jsonPath("$.preco").value(5000))
                .andExpect(jsonPath("$.qtd").value(3))
                .andExpect(jsonPath("$.qtdOperacao").value(0));

        verify(produtoService, times(1)).exibirEspecifico(id);
    }

    @Test
    public void exibirProdutoNãoExistenteTest() throws Exception {
        Long id = 1L;
        Optional<ProdutoModel> produtoModelOptional = Optional.empty();
        when(produtoService.exibirEspecifico(id)).thenReturn(produtoModelOptional);

        mockMvc.perform(get("/api/produtos/{id}", id).
                        contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Produto não encontrado!"));
    }

    @Test
    public void testAlterandoEntradaEstoque() throws Exception {
        Long id = 1L;
        AtualizarQtd atualizarQtd = new AtualizarQtd();
        atualizarQtd.setQtdOperacao(10);

        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setQtd(3);

        when(produtoService.exibirEspecifico(id)).thenReturn(Optional.of(produtoModel));
        when(produtoService.entradaProduto(eq(id), any(AtualizarQtd.class))).thenReturn(produtoModel);

        mockMvc.perform(put("/api/produtos/entrada/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(atualizarQtd)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(produtoService, times(1)).entradaProduto(eq(id), any(AtualizarQtd.class));


    }

    @Test
    public void testAlterandoSaidaEstoque() throws Exception {
        Long id = 6L; // ID de exemplo
        int quantidadeAtual = 10;
        int quantidadeOperacao = 5;

        AtualizarQtd atualizarQtd = new AtualizarQtd();
        atualizarQtd.setId(id);
        atualizarQtd.setQtdOperacao(quantidadeOperacao);

        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setId(id);
        produtoModel.setNome("Produto de Teste");
        produtoModel.setDescricao("teste");
        produtoModel.setQtd(quantidadeAtual);

        // Configurando o comportamento do serviço mockado
        when(produtoService.saidaProduto(eq(id), any())).thenReturn(produtoModel);

        mockMvc.perform(put("/api/produtos/saida/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(atualizarQtd)))
                .andExpect(status().isOk());

        verify(produtoService, times(1)).saidaProduto(eq(id), eq(atualizarQtd));
    }

    // Função auxiliar para converter objeto em JSON
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void deletarProdutoTest() throws Exception {
        Long contaId = 1L;
        ProdutoModel produtoDeletar = new ProdutoModel(1L,"iphone", "iphone 13 preto", 5000, 3,0);
        Optional<ProdutoModel> produtoOptional = Optional.of(produtoDeletar);
        when(produtoService.exibirEspecifico(contaId)).thenReturn(produtoOptional);
        mockMvc.perform(delete("/api/produtos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Produto deletada com sucesso"));
        verify(produtoService, times(1)).deletar(produtoOptional.get());

    }
    @Test
    public void testDeletarProdutoNaoExistente() throws Exception {
        Long id = 1L;
        Optional<ProdutoModel> produtoOptional = Optional.empty();
        when(produtoService.exibirEspecifico(id)).thenReturn(produtoOptional);

        mockMvc.perform(delete("/api/produtos/{id}", id).
                        contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Produto não encontrado"));
        verify(produtoService, never()).deletar(any(ProdutoModel.class));
    }

    @Test
    public void testAtualizarProduto() throws Exception {
        Long id = 1L; // ID de exemplo
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("Novo Nome");
        produtoDTO.setDescricao("Nova Descrição");
        produtoDTO.setQtd(5);
        produtoDTO.setPreco(100.0);

        ProdutoModel produtoAtualizado = new ProdutoModel();
        produtoAtualizado.setId(id);
        produtoAtualizado.setNome(produtoDTO.getNome());
        produtoAtualizado.setDescricao(produtoDTO.getDescricao());
        produtoAtualizado.setQtd(produtoDTO.getQtd());
        produtoAtualizado.setPreco(produtoDTO.getPreco());

        // Configurando o comportamento do serviço mockado
        when(produtoService.atualizarProduto(eq(id), any(ProdutoDTO.class))).thenReturn(produtoAtualizado);

        mockMvc.perform(put("/api/produtos/alterar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(produtoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value(produtoAtualizado.getNome()))
                .andExpect(jsonPath("$.descricao").value(produtoAtualizado.getDescricao()))
                .andExpect(jsonPath("$.qtd").value(produtoAtualizado.getQtd()))
                .andExpect(jsonPath("$.preco").value(produtoAtualizado.getPreco()));

        verify(produtoService, times(1)).atualizarProduto(eq(id), eq(produtoDTO));
    }







}


