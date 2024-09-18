package br.com.legalflow.auth;

import br.com.legalflow.config.filters.JwtTokenFilter;
import br.com.legalflow.entity.Organizacao;
import br.com.legalflow.service.UsuarioService;
import br.com.legalflow.security.JwtTokenProvider;
import br.com.legalflow.entity.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtTokenFilterTest {

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Usuario usuario;

    @Mock
    private Organizacao organizacao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void doFilterInternal_TokenValido() throws Exception {
        String token = "validToken";
        String email = "test@example.com";
        Long userId = 1L;
        Long organizationId = 1L;

        // Mock do request para retornar o token de autorização
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getEmailFromToken(token)).thenReturn(email);
        when(jwtTokenProvider.getIdFromToken(token)).thenReturn(userId);
        when(jwtTokenProvider.getOrganizacaoIdFromToken(token)).thenReturn(organizationId);

        // Mock do UsuarioService para retornar o usuário correspondente ao email
        when(usuarioService.findByEmail(email)).thenReturn(usuario);

        // Configura o mock do Usuario e Organizacao
        when(usuario.getId()).thenReturn(userId);
        when(usuario.getOrganizacao()).thenReturn(organizacao);
        when(organizacao.getId()).thenReturn(organizationId);

        // Usar reflexão para invocar o método protegido
        Method method = JwtTokenFilter.class.getDeclaredMethod("doFilterInternal", HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
        method.setAccessible(true);
        method.invoke(jwtTokenFilter, request, response, filterChain);

        verify(usuarioService, times(1)).findByEmail(email);
        verify(filterChain, times(1)).doFilter(request, response);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        // Verifica se o principal é um UsernamePasswordAuthenticationToken com o ID do usuário
        Object principal = ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assertTrue(principal instanceof Usuario);
        assertEquals(userId, ((Usuario) principal).getId());
    }


    @Test
    public void doFilterInternal_TokenInvalido() throws Exception {
        String token = "invalidToken";

        // Mock do request para retornar o token de autorização
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        // Usar reflexão para invocar o método protegido
        Method method = JwtTokenFilter.class.getDeclaredMethod("doFilterInternal", HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
        method.setAccessible(true);
        method.invoke(jwtTokenFilter, request, response, filterChain);

        // Verifica que o usuarioService não foi chamado
        verify(usuarioService, never()).findByEmail(anyString());
        verify(filterChain, times(1)).doFilter(request, response);

        // Assegura que o contexto de segurança não tem autenticação configurada
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
