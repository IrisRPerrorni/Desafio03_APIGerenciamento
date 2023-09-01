package com.catalisa.GerenciamentoDeEstoque.service;

import com.catalisa.GerenciamentoDeEstoque.dto.AtualizarQtd;
import com.catalisa.GerenciamentoDeEstoque.dto.ListarProduto;
import com.catalisa.GerenciamentoDeEstoque.dto.ProdutoDTO;
import com.catalisa.GerenciamentoDeEstoque.model.EntradaModel;
import com.catalisa.GerenciamentoDeEstoque.model.ProdutoModel;
import com.catalisa.GerenciamentoDeEstoque.model.SaidaModel;
import com.catalisa.GerenciamentoDeEstoque.repository.EntradaRepository;
import com.catalisa.GerenciamentoDeEstoque.repository.ProdutoRepository;
import com.catalisa.GerenciamentoDeEstoque.repository.SaidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    EntradaRepository entradaRepository;

    @Autowired
    SaidaRepository saidaRepository;

    //testado
    public ProdutoDTO cadastrarProduto(ProdutoDTO criarProduto) {
        if (criarProduto.getNome() == null || criarProduto.getDescricao() == null ||
                criarProduto.getPreco() == 0 || criarProduto.getQtd() == 0) {
            throw new IllegalArgumentException("Os campos não podem estar vazios");
        }
        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setNome(criarProduto.getNome());
        produtoModel.setDescricao(criarProduto.getDescricao());
        produtoModel.setPreco(criarProduto.getPreco());
        produtoModel.setQtd(criarProduto.getQtd());
        produtoModel.setQtdOperacao(0);
        produtoRepository.save(produtoModel);

        ProdutoDTO cadastroProduto = new ProdutoDTO();
        cadastroProduto.setNome(produtoModel.getNome());
        cadastroProduto.setDescricao(produtoModel.getDescricao());
        cadastroProduto.setPreco(produtoModel.getPreco());
        cadastroProduto.setQtd(produtoModel.getQtd());
        return cadastroProduto;
    }

    //testado
    public List<ListarProduto> exibirProdutos() {
        List<ProdutoModel> produtoModels = produtoRepository.findAll();
        List<ListarProduto> listarDTO = new ArrayList<>();
        for (ProdutoModel produto : produtoModels) {
            ListarProduto produtoDTO = new ListarProduto();
            produtoDTO.setId(produto.getId());
            produtoDTO.setNome(produto.getNome());
            produtoDTO.setDescricao(produto.getDescricao());
            produtoDTO.setPreco(produto.getPreco());
            produtoDTO.setQtd(produto.getQtd());
            listarDTO.add(produtoDTO);

        }
        return listarDTO;
    }

    //testado caso achar o id , e caso não existir o id
    public Optional<ProdutoModel> exibirEspecifico(Long id) {
        return produtoRepository.findById(id);

    }

    public ProdutoModel entradaProduto(Long id, AtualizarQtd atualizarQtd) {
        Optional<ProdutoModel> produtoOptional = exibirEspecifico(id);
        if (produtoOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não cadastrado");
        }
        ProdutoModel produto = produtoOptional.get();
        produto.setQtd(produto.getQtd() + atualizarQtd.getQtdOperacao());
        produto.setQtdOperacao(atualizarQtd.getQtdOperacao());
        ProdutoModel produtoEntradaAtualizado = produtoRepository.save(produto);
        salvarHistoricoEntrada(produtoEntradaAtualizado);
        return produtoEntradaAtualizado;
    }

    public void salvarHistoricoEntrada(ProdutoModel produtoModel) {
        EntradaModel entradaModel = new EntradaModel(produtoModel.getId(), produtoModel.getNome(),
                produtoModel.getQtdOperacao(), LocalDateTime.now(ZoneId.of("UTC")));

        entradaRepository.save(entradaModel);

    }


    public ProdutoModel saidaProduto(Long id, AtualizarQtd atualizarQtd) {
        Optional<ProdutoModel> produtoOptional = exibirEspecifico(id);
        if (produtoOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não cadastrado");
        }
        ProdutoModel produto = produtoOptional.get();
        if (produto.getQtd() <= atualizarQtd.getQtdOperacao()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possivel retirar essa quantidade");
        } else {
            produto.setQtd(produto.getQtd() - atualizarQtd.getQtdOperacao());
            produto.setQtdOperacao(atualizarQtd.getQtdOperacao());
            ProdutoModel produtoSaidaAtualizado = produtoRepository.save(produto);
            salvarHistoricoSaida(produtoSaidaAtualizado);
            return produtoSaidaAtualizado;
        }
    }

    public void salvarHistoricoSaida(ProdutoModel produtoModel) {
        SaidaModel saidaModel = new SaidaModel(produtoModel.getId(), produtoModel.getNome(),
                produtoModel.getQtdOperacao(), LocalDateTime.now(ZoneId.of("UTC")));

        saidaRepository.save(saidaModel);

    }

    public ProdutoModel atualizarProduto(Long id, ProdutoDTO produtoDTO) {
        ProdutoModel produtoModel = produtoRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Produto não encontrado"));

        if (produtoDTO.getNome() != null) {
            produtoModel.setNome(produtoDTO.getNome());
        }
        if (produtoDTO.getDescricao() != null) {
            produtoModel.setDescricao(produtoDTO.getDescricao());
        }
        if (produtoDTO.getQtd() != 0) {
            produtoModel.setQtd(produtoDTO.getQtd());
        }
        if (produtoDTO.getPreco() != 0) {
            produtoModel.setPreco(produtoDTO.getPreco());
        }
        return produtoRepository.save(produtoModel);

    }

    //testado
    public void deletar(ProdutoModel produtoModel) {

        produtoRepository.delete(produtoModel);
    }
}




