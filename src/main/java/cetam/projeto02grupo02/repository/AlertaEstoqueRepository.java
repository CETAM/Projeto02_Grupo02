package cetam.projeto02grupo02.repository;

import cetam.projeto02grupo02.model.AlertaEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertaEstoqueRepository extends JpaRepository<AlertaEstoque, Long> {
    List<AlertaEstoque> findByStatusAlerta(String statusAlerta);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM AlertaEstoque a WHERE a.produto.idProduto = :idProduto")
    void deleteByProdutoIdProduto(@org.springframework.data.repository.query.Param("idProduto") Long idProduto);
}