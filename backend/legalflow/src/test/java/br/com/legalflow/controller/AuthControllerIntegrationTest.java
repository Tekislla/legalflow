package br.com.legalflow.controller;

import br.com.legalflow.entity.Usuario;
import br.com.legalflow.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        usuarioRepository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setSenhaCrypto(passwordEncoder.encode("senha"));
        usuario.setIdOrganizacao(1l);
        usuario.setRole("ROLE_USER");
        usuarioRepository.save(usuario);
    }

    @Test
    public void login_Sucesso() throws Exception {
        String loginRequest = "{\"email\":\"test@example.com\", \"senha\":\"senha\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void login_Falha_UsuarioNaoEncontrado() throws Exception {
        String loginRequest = "{\"email\":\"naoencontrado@example.com\", \"senha\":\"senha\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void login_Falha_SenhaIncorreta() throws Exception {
        String loginRequest = "{\"email\":\"test@example.com\", \"senha\":\"senhaIncorreta\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized());
    }
}
