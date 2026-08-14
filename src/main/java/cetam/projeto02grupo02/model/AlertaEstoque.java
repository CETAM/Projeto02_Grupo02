package cetam.projeto02grupo02.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AlertaEstoque")
public class AlertaEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlerta;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @Column(name = "mensagem", nullable = false, length = 255)
    private String mensagem;

    @Column(name = "data_alerta")
    private LocalDateTime dataAlerta = LocalDateTime.now();

    @Column(name = "status_alerta", length = 20)
    private String statusAlerta = "PENDENTE";

    public AlertaEstoque() {}

    public Long getIdAlerta() { return idAlerta; }
    public void setIdAlerta(Long idAlerta) { this.idAlerta = idAlerta; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public LocalDateTime getDataAlerta() { return dataAlerta; }
    public void setDataAlerta(LocalDateTime dataAlerta) { this.dataAlerta = dataAlerta; }

    public String getStatusAlerta() { return statusAlerta; }
    public void setStatusAlerta(String statusAlerta) { this.statusAlerta = statusAlerta; }
}