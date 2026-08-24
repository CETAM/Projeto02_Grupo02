package cetam.projeto02grupo02.repository;

import cetam.projeto02grupo02.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProdutoIdProduto(Long idProduto);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM Estoque e WHERE e.produto.idProduto = :idProduto")
    void deleteByProdutoIdProduto(@org.springframework.data.repository.query.Param("idProduto") Long idProduto);
}