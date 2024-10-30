package br.com.legalflow.auth;

import br.com.legalflow.config.filters.JwtTokenFilter;
import br.com.legalflow.entity.Organizacao;
import br.com.legalflow.entity.Usuario;
import br.com.legalflow.security.JwtTokenProvider;
import br.com.legalflow.service.UsuarioService;
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

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenFilterTest {

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
    void doFilterInternal_TokenValido() throws Exception {
        String token = "validToken";
        String email = "test@example.com";
        Long userId = 1L;
        Long organizationId = 1L;

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getEmailFromToken(token)).thenReturn(email);
        when(jwtTokenProvider.getIdFromToken(token)).thenReturn(userId);
        when(jwtTokenProvider.getOrganizacaoIdFromToken(token)).thenReturn(organizationId);

        when(usuarioService.findByEmail(email)).thenReturn(usuario);

        when(usuario.getId()).thenReturn(userId);
        when(usuario.getOrganizacao()).thenReturn(organizacao);
        when(organizacao.getId()).thenReturn(organizationId);

        Method method = JwtTokenFilter.class.getDeclaredMethod("doFilterInternal", HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
        method.setAccessible(true);
        method.invoke(jwtTokenFilter, request, response, filterChain);

        verify(usuarioService, times(1)).findByEmail(email);
        verify(filterChain, times(1)).doFilter(request, response);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assertTrue(principal instanceof Usuario);
        assertEquals(userId, ((Usuario) principal).getId());
    }

    @Test
    void doFilterInternal_TokenInvalido() throws Exception {
        String token = "invalidToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        Method method = JwtTokenFilter.class.getDeclaredMethod("doFilterInternal", HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
        method.setAccessible(true);
        method.invoke(jwtTokenFilter, request, response, filterChain);

        verify(usuarioService, never()).findByEmail(anyString());
        verify(filterChain, times(1)).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
