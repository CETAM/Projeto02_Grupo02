package cetam.projeto02grupo02.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstoque;

    @OneToOne
    @JoinColumn(name = "id_produto", nullable = false, unique = true)
    private Produto produto;

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