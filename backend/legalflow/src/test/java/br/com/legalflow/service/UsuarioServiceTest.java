package br.com.legalflow.service;

import br.com.legalflow.entity.Usuario;
import br.com.legalflow.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    public UsuarioServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAutenticarUsuario_Sucesso() throws Exception {
        String email = "user@example.com";
        String senha = "senha123";

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenhaCrypto(passwordEncoder.encode(senha));

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, usuario.getSenhaCrypto())).thenReturn(true);

        Usuario resultado = usuarioService.autenticarUsuario(email, senha);
        assertEquals(email, resultado.getEmail());
    }

    @Test
    public void testAutenticarUsuario_UsuarioNaoEncontrado() {
        String email = "user@example.com";
        String senha = "senha123";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () ->
                usuarioService.autenticarUsuario(email, senha)
        );
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    public void testAutenticarUsuario_SenhaIncorreta() {
        String email = "user@example.com";
        String senha = "senha123";

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenhaCrypto(passwordEncoder.encode("senhaErrada"));

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, usuario.getSenhaCrypto())).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () ->
                usuarioService.autenticarUsuario(email, senha)
        );
        assertEquals("Senha incorreta", exception.getMessage());
    }
}
