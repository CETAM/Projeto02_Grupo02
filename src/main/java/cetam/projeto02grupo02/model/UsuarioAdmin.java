package cetam.projeto02grupo02.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UsuarioAdmin")
public class UsuarioAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAdmin;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "login", nullable = false, unique = true, length = 50)
    private String login;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    public UsuarioAdmin() {}

    public Long getIdAdmin() { return idAdmin; }
    public void setIdAdmin(Long idAdmin) { this.idAdmin = idAdmin; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}