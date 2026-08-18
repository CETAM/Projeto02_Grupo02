package cetam.projeto02grupo02.repository;

import cetam.projeto02grupo02.model.AlertaEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertaEstoqueRepository extends JpaRepository<AlertaEstoque, Long> {
    List<AlertaEstoque> findByStatusAlerta(String statusAlerta);
}