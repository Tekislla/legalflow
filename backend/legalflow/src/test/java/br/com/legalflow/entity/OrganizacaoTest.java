package br.com.legalflow.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class OrganizacaoTest {

    private Organizacao organizacao1;
    private Organizacao organizacao2;

    @BeforeEach
    void setUp() {
        organizacao1 = new Organizacao();
        organizacao1.setId(1L);
        organizacao1.setNome("Organizacao A");
        organizacao1.setDocumento("123456789");
        organizacao1.setQuadros(List.of(new Quadro()));
        organizacao1.setUsuarios(List.of(new Usuario()));

        organizacao2 = new Organizacao();
        organizacao2.setId(organizacao1.getId());
        organizacao2.setNome(organizacao1.getNome());
        organizacao2.setDocumento(organizacao1.getDocumento());
        organizacao2.setQuadros(organizacao1.getQuadros());
        organizacao2.setUsuarios(organizacao1.getUsuarios());
    }

    @Test
    void testEquals() {
        assertEquals(organizacao1, organizacao2);
    }

    @Test
    void testHashCode() {
        assertEquals(organizacao1.hashCode(), organizacao2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(organizacao1.toString(), organizacao2.toString());
    }

    @Test
    void testNotEquals() {
        organizacao2.setId(2L);
        assertNotEquals(organizacao1, organizacao2);
    }
}
