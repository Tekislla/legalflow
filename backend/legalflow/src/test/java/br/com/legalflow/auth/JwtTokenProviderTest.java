package br.com.legalflow.auth;

import br.com.legalflow.entity.Usuario;
import br.com.legalflow.utils.JwtTokenProvider;
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
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpiration", 3600000L);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@example.com");
        usuario.setIdOrganizacao(1L);
        usuario.setRole("ROLE_USER");
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
}
