package br.com.legalflow.auth;

import br.com.legalflow.entity.Organizacao;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "minhaChaveSecreta");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationDays", 15);

        Organizacao organizacao = new Organizacao();
        organizacao.setId(1L);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@example.com");
        usuario.setOrganizacao(organizacao);
        usuario.setRole("ADMIN");
    }

    @Test
    void gerarToken_Sucesso() {
        String token = jwtTokenProvider.gerarToken(usuario);
        assertNotNull(token);
    }

    @Test
    void validateToken_Falha() {
        assertFalse(jwtTokenProvider.validateToken("token"));
    }

    @Test
    void validateToken_Sucesso() {
        String token = jwtTokenProvider.gerarToken(usuario);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void getEmailFromToken_Sucesso() {
        String token = jwtTokenProvider.gerarToken(usuario);
        String email = jwtTokenProvider.getEmailFromToken(token);
        assertEquals("test@example.com", email);
    }

    @Test
    void getOrganizacaoIdFromToken_Sucesso() {
        String token = jwtTokenProvider.gerarToken(usuario);
        Long organizacaoId = jwtTokenProvider.getOrganizacaoIdFromToken(token);
        assertEquals(1L, organizacaoId);
    }

    @Test
    void getRoleFromToken_Sucesso() {
        String token = jwtTokenProvider.gerarToken(usuario);
        String role = jwtTokenProvider.getRoleFromToken(token);
        assertEquals("ADMIN", role);
    }

    @Test
    void getIdFromToken_Sucesso() {
        String token = jwtTokenProvider.gerarToken(usuario);
        Long id = jwtTokenProvider.getIdFromToken(token);
        assertEquals(1L, id);
    }
}
