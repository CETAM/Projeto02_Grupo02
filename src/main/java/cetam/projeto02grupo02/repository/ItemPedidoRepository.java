package cetam.projeto02grupo02.repository;

import cetam.projeto02grupo02.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    List<ItemPedido> findByPedidoIdPedido(Long idPedido);
    boolean existsByProdutoIdProduto(Long idProduto);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM ItemPedido i WHERE i.produto.idProduto = :idProduto")
    void deleteByProdutoIdProduto(@org.springframework.data.repository.query.Param("idProduto") Long idProduto);
}