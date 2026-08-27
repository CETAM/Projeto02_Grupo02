package cetam.projeto02grupo02.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "Estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstoque;

    @Version
    private Long version;

    @OneToOne
    @JoinColumn(name = "id_produto", nullable = false, unique = true)
    private Produto produto;

    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa")
    @Column(name = "quantidade_atual")
    private Integer quantidadeAtual = 0;

    public Estoque() {}

    public Long getIdEstoque() { return idEstoque; }
    public void setIdEstoque(Long idEstoque) { this.idEstoque = idEstoque; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Integer getQuantidadeAtual() { return quantidadeAtual; }
    public void setQuantidadeAtual(Integer quantidadeAtual) { this.quantidadeAtual = quantidadeAtual; }
}