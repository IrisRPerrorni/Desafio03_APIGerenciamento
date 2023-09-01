package com.catalisa.GerenciamentoDeEstoque.serviceTest;

import com.catalisa.GerenciamentoDeEstoque.dto.AtualizarQtd;
import com.catalisa.GerenciamentoDeEstoque.dto.ListarProduto;
import com.catalisa.GerenciamentoDeEstoque.dto.ProdutoDTO;
import com.catalisa.GerenciamentoDeEstoque.model.EntradaModel;
import com.catalisa.GerenciamentoDeEstoque.model.ProdutoModel;
import com.catalisa.GerenciamentoDeEstoque.model.SaidaModel;
import com.catalisa.GerenciamentoDeEstoque.repository.EntradaRepository;
import com.catalisa.GerenciamentoDeEstoque.repository.ProdutoRepository;
import com.catalisa.GerenciamentoDeEstoque.repository.SaidaRepository;
import com.catalisa.GerenciamentoDeEstoque.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private EntradaRepository entradaRepository;

    @Mock
    private SaidaRepository saidaRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCadastrarProduto() {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("Produto Teste");
        produtoDTO.setDescricao("Descrição do Produto Teste");
        produtoDTO.setPreco(10.0);
        produtoDTO.setQtd(5);

        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setNome(produtoDTO.getNome());
        produtoModel.setDescricao(produtoDTO.getDescricao());
        produtoModel.setPreco(produtoDTO.getPreco());
        produtoModel.setQtd(produtoDTO.getQtd());

        when(produtoRepository.save(any(ProdutoModel.class))).thenReturn(produtoModel);

        ProdutoDTO resultado = produtoService.cadastrarProduto(produtoDTO);

        assertEquals(produtoDTO.getNome(), resultado.getNome());
        assertEquals(produtoDTO.getDescricao(), resultado.getDescricao());
        assertEquals(produtoDTO.getPreco(), resultado.getPreco());
        assertEquals(produtoDTO.getQtd(), resultado.getQtd());

        verify(produtoRepository, times(1)).save(any(ProdutoModel.class));
    }

    @Test
    public void testExibirProdutos() {
        List<ProdutoModel> produtoModels = new ArrayList<>();
        produtoModels.add(new ProdutoModel(1L, "Produto 1", "Descrição do Produto 1", 10.0,
                5, 0));
        produtoModels.add(new ProdutoModel(2L, "Produto 2", "Descrição do Produto 2", 15.0,
                8, 0));

        /// Isso configura o comportamento do mock produtoRepository para que, quando o método findAll() for chamado,
        // ele retorne a lista produtoModels

        when(produtoRepository.findAll()).thenReturn(produtoModels);

        // chamando o método que queremos testar (exibirProdutos() do ProdutoService) e armazenando o resultado em uma lista.
        List<ListarProduto> resultado = produtoService.exibirProdutos();

        assertEquals(produtoModels.size(), resultado.size());
        assertEquals(produtoModels.get(0).getId(), resultado.get(0).getId());
        assertEquals(produtoModels.get(0).getNome(), resultado.get(0).getNome());
        assertEquals(produtoModels.get(0).getDescricao(), resultado.get(0).getDescricao());
        assertEquals(produtoModels.get(0).getPreco(), resultado.get(0).getPreco());
        assertEquals(produtoModels.get(0).getQtd(), resultado.get(0).getQtd());
        assertEquals(produtoModels.get(1).getId(), resultado.get(1).getId());
        assertEquals(produtoModels.get(1).getNome(), resultado.get(1).getNome());
        assertEquals(produtoModels.get(1).getDescricao(), resultado.get(1).getDescricao());
        assertEquals(produtoModels.get(1).getPreco(), resultado.get(1).getPreco());
        assertEquals(produtoModels.get(1).getQtd(), resultado.get(1).getQtd());

        // Verificar se o método findAll foi chamado no repositório
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    public void testexibirEspecifico() {
        Long id = 1L;
        ProdutoModel produtoExistente = new ProdutoModel(id, "Produto 1", "Descrição do Produto 1",
                10.0, 5, 0);
        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoExistente));
        Optional<ProdutoModel> resultadoProduto = produtoService.exibirEspecifico(id);

        assertTrue(resultadoProduto.isPresent());
        assertEquals(produtoExistente, resultadoProduto.get());
        verify(produtoRepository, times(1)).findById(id);
    }

    @Test
    public void testExibirEspecificoNaoExisteId() {
        Long id = 10L;
        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ProdutoModel> resultadoNaoEncontrado = produtoService.exibirEspecifico(id);

        assertFalse(resultadoNaoEncontrado.isPresent());

        verify(produtoRepository, times(1)).findById(id);

    }

    @Test
    public void testEntradaProduto() {
        Long id = 1L;
        int qtdOperacao = 5;
        ProdutoModel produtoEntrada = new ProdutoModel();
        produtoEntrada.setId(id);
        produtoEntrada.setNome("Produto 1");
        produtoEntrada.setDescricao("Descrição do Produto 1");
        produtoEntrada.setPreco(10.0);
        produtoEntrada.setQtd(10);
        AtualizarQtd atualizarQtd = new AtualizarQtd(id, qtdOperacao);

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoEntrada));
        when(produtoRepository.save(produtoEntrada)).thenReturn(produtoEntrada);

        ProdutoModel resultadoEntrada = produtoService.entradaProduto(id, atualizarQtd);

        assertEquals(15, resultadoEntrada.getQtd());
        assertEquals(qtdOperacao, resultadoEntrada.getQtdOperacao());

        verify(produtoRepository, times(1)).save(produtoEntrada);
        verify(entradaRepository, times(1)).save(any(EntradaModel.class));
    }

    @Test
    public void testSalvarHistoricoEntrada() {
        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setId(1L);
        produtoModel.setNome("Produto A");
        produtoModel.setQtdOperacao(10);

        produtoService.salvarHistoricoEntrada(produtoModel);

        verify(entradaRepository, times(1)).save(any(EntradaModel.class));
    }

    @Test
    public void testEntradaProdutoProdutoNaoEncontrado() {
        Long idProdutoNaoExistente = 20L;

        // Configurar o comportamento simulado do produtoRepository
        Mockito.when(produtoRepository.findById(idProdutoNaoExistente))
                .thenReturn(Optional.empty());

        // Verificar se a exceção esperada é lançada
        assertThrows(ResponseStatusException.class, () -> {
            produtoService.entradaProduto(idProdutoNaoExistente, new AtualizarQtd(10));
        });

    }
    @Test
    public void testSaidaProduto() {
        Long id = 1L;
        int qtdOperacao = 5;
        ProdutoModel produtoSaida = new ProdutoModel();
        produtoSaida.setId(id);
        produtoSaida.setNome("Produto 1");
        produtoSaida.setDescricao("Descrição do Produto 1");
        produtoSaida.setPreco(10.0);
        produtoSaida.setQtd(10);
        AtualizarQtd atualizarQtd = new AtualizarQtd(id, qtdOperacao);

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoSaida));
        when(produtoRepository.save(produtoSaida)).thenReturn(produtoSaida);

        ProdutoModel resultadoSaida = produtoService.saidaProduto(id, atualizarQtd);

        assertEquals(5, resultadoSaida.getQtd());
        assertEquals(qtdOperacao, resultadoSaida.getQtdOperacao());

        verify(produtoRepository, times(1)).save(produtoSaida);
        verify(saidaRepository, times(1)).save(any(SaidaModel.class));
    }

    @Test
    public void testSalvarHistoricoSaida() {
        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setId(1L);
        produtoModel.setNome("Produto A");
        produtoModel.setQtdOperacao(10);

        produtoService.salvarHistoricoSaida(produtoModel);

        verify(saidaRepository, times(1)).save(any(SaidaModel.class));
    }

    @Test
    public void testSaidaProdutoProdutoNaoEncontrado() {
        Long idProdutoNaoExistente = 20L;

        // Configurar o comportamento simulado do produtoRepository
        Mockito.when(produtoRepository.findById(idProdutoNaoExistente))
                .thenReturn(Optional.empty());

        // Verificar se a exceção esperada é lançada
        assertThrows(ResponseStatusException.class, () -> {
            produtoService.saidaProduto(idProdutoNaoExistente, new AtualizarQtd(10));
        });

    }

    @Test
    public void testAtualizarProduto() {
        // Criação de dados de exemplo
        Long id = 1L;
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("teste");
        produtoDTO.setDescricao("testando");
        produtoDTO.setPreco(10);
        produtoDTO.setQtd(10);

        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setId(id);
        produtoModel.setNome("Nome Antigo");
        produtoModel.setDescricao("descreva");
        produtoModel.setPreco(5);
        produtoModel.setQtd(5);

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoModel));
        when(produtoRepository.save(any(ProdutoModel.class))).thenReturn(produtoModel);


        // Chamada ao método de atualização
        ProdutoModel resultado = produtoService.atualizarProduto(id, produtoDTO);

        // Verificação dos resultados
        assertEquals(produtoModel.getNome(), resultado.getNome());
        assertEquals(produtoModel.getDescricao(), resultado.getDescricao());
        assertEquals(produtoModel.getQtd(), resultado.getQtd());
        assertEquals(produtoModel.getPreco(), resultado.getPreco());

        Mockito.verify(produtoRepository, Mockito.times(1)).save(produtoModel);
    }
    @Test
    public void testDeletarProduto(){
        Long id = 1L;
        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setId(id);
        doNothing().when(produtoRepository).delete(produtoModel);
        produtoService.deletar(produtoModel);
        verify(produtoRepository, times(1)).delete(produtoModel);



    }
}

