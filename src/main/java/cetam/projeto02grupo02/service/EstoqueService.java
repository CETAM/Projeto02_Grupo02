package cetam.projeto02grupo02.service;

import cetam.projeto02grupo02.model.AlertaEstoque;
import cetam.projeto02grupo02.model.Estoque;
import cetam.projeto02grupo02.model.Produto;
import cetam.projeto02grupo02.repository.AlertaEstoqueRepository;
import cetam.projeto02grupo02.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private AlertaEstoqueRepository alertaEstoqueRepository;

    public List<Estoque> listarTodos() {
        return estoqueRepository.findAll();
    }

    public Optional<Estoque> buscarPorProdutoId(Long idProduto) {
        return estoqueRepository.findByProdutoIdProduto(idProduto);
    }

    @Transactional
    public void atualizarQuantidade(Long idProduto, Integer novaQuantidade) {
        Estoque estoque = estoqueRepository.findByProdutoIdProduto(idProduto)
                .orElseThrow(() -> new IllegalArgumentException("Registro de estoque não encontrado para o produto ID: " + idProduto));

        estoque.setQuantidadeAtual(novaQuantidade);
        estoqueRepository.save(estoque);
        verificarEGerarAlerta(estoque.getProduto(), novaQuantidade);
    }

    @Transactional
    public void verificarDisponibilidade(Produto produto, Integer quantidadeVendida) {
        Estoque estoque = estoqueRepository.findByProdutoIdProduto(produto.getIdProduto())
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado para o produto: " + produto.getNomeProduto()));

        if (estoque.getQuantidadeAtual() < quantidadeVendida) {
            throw new IllegalStateException("Estoque insuficiente para o produto: " + produto.getNomeProduto());
        }
    }

    @Transactional
    public void baixarEstoque(Produto produto, Integer quantidadeVendida) {
        Estoque estoque = estoqueRepository.findByProdutoIdProduto(produto.getIdProduto())
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado para o produto: " + produto.getNomeProduto()));

        if (estoque.getQuantidadeAtual() < quantidadeVendida) {
            throw new IllegalStateException("Estoque insuficiente para o produto: " + produto.getNomeProduto());
        }

        int novoEstoque = estoque.getQuantidadeAtual() - quantidadeVendida;
        estoque.setQuantidadeAtual(novoEstoque);
        estoqueRepository.save(estoque);

        verificarEGerarAlerta(produto, novoEstoque);
    }

    private void verificarEGerarAlerta(Produto produto, int estoqueAtual) {
        alertaEstoqueRepository.deleteByProdutoIdProduto(produto.getIdProduto());

        if (estoqueAtual <= produto.getEstoqueMinimo()) {
            AlertaEstoque alerta = new AlertaEstoque();
            alerta.setProduto(produto);
            alerta.setMensagem("Estoque crítico para '" + produto.getNomeProduto() + "'. Qtd atual: " + estoqueAtual + " (Mínimo: " + produto.getEstoqueMinimo() + ")");
            alertaEstoqueRepository.save(alerta);
        }
    }
}