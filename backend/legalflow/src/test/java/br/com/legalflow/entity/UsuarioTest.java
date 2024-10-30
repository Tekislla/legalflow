package br.com.legalflow.entity;

import br.com.legalflow.enums.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UsuarioTest {

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("User A");
        usuario1.setEmail("usera@example.com");
        usuario1.setSenhaCrypto("123123134");
        usuario1.setAtivo(true);
        usuario1.setRole(RoleEnum.ADMIN.name());
        usuario1.setDataCadastro(new Date());
        usuario1.setDataAtualizacao(new Date());
        usuario1.setOrganizacao(new Organizacao());
        usuario1.setQuadros(List.of(new Quadro()));


        usuario2 = new Usuario();
        usuario2.setId(usuario1.getId());
        usuario2.setNome(usuario1.getNome());
        usuario2.setEmail(usuario1.getEmail());
        usuario2.setSenhaCrypto(usuario1.getSenhaCrypto());
        usuario2.setAtivo(usuario1.isAtivo());
        usuario2.setRole(usuario1.getRole());
        usuario2.setDataCadastro(usuario1.getDataCadastro());
        usuario2.setDataAtualizacao(usuario1.getDataAtualizacao());
        usuario2.setOrganizacao(usuario1.getOrganizacao());
        usuario2.setQuadros(usuario1.getQuadros());
    }

    @Test
    void testEquals() {
        assertEquals(usuario1, usuario2);
    }

    @Test
    void testHashCode() {
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(usuario1.toString(), usuario2.toString());
    }

    @Test
    void testNotEquals() {
        usuario2.setId(2L);
        assertNotEquals(usuario1, usuario2);
    }
}
