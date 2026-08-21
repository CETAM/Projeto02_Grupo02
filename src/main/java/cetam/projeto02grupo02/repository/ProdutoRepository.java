package cetam.projeto02grupo02.repository;

import cetam.projeto02grupo02.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByCategoriaIdCategoria(Long idCategoria);
    List<Produto> findByAtivoTrue();
}