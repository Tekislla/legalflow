package br.com.legalflow.service;

import br.com.legalflow.dto.request.CadastroRequestDTO;
import br.com.legalflow.entity.Organizacao;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.exception.usuario.CredenciaisInvalidasException;
import br.com.legalflow.exception.usuario.UsuarioInativoException;
import br.com.legalflow.exception.usuario.UsuarioJaCadastradoException;
import br.com.legalflow.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldCadastrarUsuario() {
        CadastroRequestDTO dto = new CadastroRequestDTO();
        dto.setEmail("test@test.com");
        dto.setNome("Test");
        dto.setSenha("password");

        Organizacao organizacao = new Organizacao();
        when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario());

        Usuario usuario = usuarioService.cadastrarUsuario(dto, organizacao);

        assertNotNull(usuario);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void shouldThrowUsuarioJaCadastradoException() {
        CadastroRequestDTO dto = new CadastroRequestDTO();
        dto.setEmail("test@test.com");

        when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new Usuario()));

        assertThrows(UsuarioJaCadastradoException.class, () -> {
            usuarioService.cadastrarUsuario(dto, new Organizacao());
        });
    }

    @Test
    void shouldAutenticarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setAtivo(true);
        usuario.setSenhaCrypto("encodedPassword");

        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password", usuario.getSenhaCrypto())).thenReturn(true);

        Usuario result = usuarioService.autenticarUsuario("test@test.com", "password");

        assertNotNull(result);
    }

    @Test
    void shouldThrowUsuarioInativoException() {
        Usuario usuario = new Usuario();
        usuario.setAtivo(false);

        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));

        assertThrows(UsuarioInativoException.class, () -> {
            usuarioService.autenticarUsuario("test@test.com", "password");
        });
    }

    @Test
    void shouldThrowCredenciaisInvalidasException() {
        Usuario usuario = new Usuario();
        usuario.setAtivo(true);
        usuario.setSenhaCrypto("encodedPassword");

        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("wrongPassword", usuario.getSenhaCrypto())).thenReturn(false);

        assertThrows(CredenciaisInvalidasException.class, () -> {
            usuarioService.autenticarUsuario("test@test.com", "wrongPassword");
        });
    }
}
