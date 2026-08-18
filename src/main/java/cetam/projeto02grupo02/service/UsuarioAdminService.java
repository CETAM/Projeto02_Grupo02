package cetam.projeto02grupo02.service;

import cetam.projeto02grupo02.model.UsuarioAdmin;
import cetam.projeto02grupo02.repository.UsuarioAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioAdminService {

    @Autowired
    private UsuarioAdminRepository usuarioAdminRepository;

    public Optional<UsuarioAdmin> autenticar(String login, String senha) {
        return usuarioAdminRepository.findByLoginAndSenha(login, senha);
    }
}