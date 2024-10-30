package br.com.legalflow.service;

import br.com.legalflow.dto.request.CadastroRequestDTO;
import br.com.legalflow.dto.request.EditarUsuarioRequestDTO;
import br.com.legalflow.entity.Organizacao;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.enums.RoleEnum;
import br.com.legalflow.exception.usuario.CredenciaisInvalidasException;
import br.com.legalflow.exception.usuario.UsuarioInativoException;
import br.com.legalflow.exception.usuario.UsuarioJaCadastradoException;
import br.com.legalflow.exception.usuario.UsuarioNaoEncontradoException;
import br.com.legalflow.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
    @Mock
    private QuadroService quadroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldCadastrarUsuarioAdmin() {
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
    void shouldCadastrarUsuarioComum() {
        CadastroRequestDTO dto = new CadastroRequestDTO();
        dto.setEmail("test2@test.com");
        dto.setNome("Test");
        dto.setSenha("password");
        dto.setAdministrador(false);

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

    @Test
    void shouldEditarUsuario() {
        EditarUsuarioRequestDTO dto = new EditarUsuarioRequestDTO();
        dto.setId(1L);
        dto.setSenha("newPassword");
        dto.setAtivo(true);
        dto.setAdministrador(false);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setOrganizacao(new Organizacao());
        usuario.setNome("Test");
        usuario.setEmail("test@test.com");
        usuario.setSenhaCrypto("encodedPassword");
        usuario.setDataCadastro(new Date());
        usuario.setDataAtualizacao(new Date());
        usuario.setAtivo(true);
        usuario.setRole(RoleEnum.ADMIN.toString());

        when(usuarioRepository.findById(dto.getId())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("encodedNewPassword");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario result = usuarioService.editarUsuario(dto);

        assertNotNull(result);
        assertEquals("encodedNewPassword", result.getSenhaCrypto());
        assertEquals(dto.isAtivo(), result.isAtivo());
        assertEquals(RoleEnum.USER.toString(), result.getRole());
        verify(usuarioRepository, times(1)).save(result);
    }

    @Test
    void shouldThrowUsuarioNaoEncontradoExceptionOnEdit() {
        EditarUsuarioRequestDTO dto = new EditarUsuarioRequestDTO();
        dto.setId(1L);

        when(usuarioRepository.findById(dto.getId())).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.editarUsuario(dto));
    }

    @Test
    void shouldFindById() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.findById(1L);

        assertNotNull(result);
    }

    @Test
    void shouldThrowUsuarioNaoEncontradoExceptionOnFindById() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.findById(1L));
    }

    @Test
    void shouldFindByEmail() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");

        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.findByEmail("test@test.com");

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void shouldThrowUsuarioNaoEncontradoExceptionOnFindByEmail() {
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.findByEmail("test@test.com"));
    }

    @Test
    void shouldFindByOrganizacaoId() {
        List<Usuario> usuarios = List.of(new Usuario());

        when(usuarioRepository.findByOrganizacaoId(1L)).thenReturn(usuarios);

        List<Usuario> result = usuarioService.findByOrganizacaoId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldExcluirUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setQuadros(List.of());

        doNothing().when(quadroService).deletarQuadro(any());
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.excluirUsuario(usuario);

        verify(quadroService, times(0)).deletarQuadro(any());
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldAtivaDesativaUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setAtivo(false);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        usuarioService.ativaDesativaUsuario(1L, true);

        assertTrue(usuario.isAtivo());
        verify(usuarioRepository, times(1)).save(usuario);
    }
}
