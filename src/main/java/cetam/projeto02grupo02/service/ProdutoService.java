package cetam.projeto02grupo02.service;

import cetam.projeto02grupo02.model.Estoque;
import cetam.projeto02grupo02.model.Produto;
import cetam.projeto02grupo02.repository.EstoqueRepository;
import cetam.projeto02grupo02.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    @Transactional
    public Produto salvar(Produto produto, Integer quantidadeEstoqueInicial) {
        boolean isNovo = produto.getIdProduto() == null;
        Produto salvo = produtoRepository.save(produto);

        if (isNovo) {
            Estoque estoque = new Estoque();
            estoque.setProduto(salvo);
            estoque.setQuantidadeAtual(quantidadeEstoqueInicial != null ? quantidadeEstoqueInicial : 0);
            estoqueRepository.save(estoque);
        }

        return salvo;
    }

    @Transactional
    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }
}