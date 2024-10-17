package br.com.legalflow.auth;

import br.com.legalflow.entity.Organizacao;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
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
    public void gerarToken_Sucesso() {
        String token = jwtTokenProvider.gerarToken(usuario);
        assertNotNull(token);
    }

    @Test
    public void validateToken_Sucesso() {
        String token = jwtTokenProvider.gerarToken(usuario);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    public void getEmailFromToken_Sucesso() {
        String token = jwtTokenProvider.gerarToken(usuario);
        String email = jwtTokenProvider.getEmailFromToken(token);
        assertEquals("test@example.com", email);
    }

    @Test
    public void getOrganizacaoIdFromToken_Sucesso() {
        String token = jwtTokenProvider.gerarToken(usuario);
        Long organizacaoId = jwtTokenProvider.getOrganizacaoIdFromToken(token);
        assertEquals(1L, organizacaoId);
    }

    @Test
    public void getRoleFromToken_Sucesso() {
        String token = jwtTokenProvider.gerarToken(usuario);
        String role = jwtTokenProvider.getRoleFromToken(token);
        assertEquals("ADMIN", role);
    }
}
