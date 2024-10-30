package br.com.legalflow.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class QuadroTest {

    private Quadro quadro1;
    private Quadro quadro2;

    @BeforeEach
    void setUp() {
        quadro1 = new Quadro();
        quadro1.setId(1L);
        quadro1.setNome("Quadro A");
        quadro1.setOrganizacao(new Organizacao());
        quadro1.setUsuario(new Usuario());
        quadro1.setProcessos(List.of(new Processo()));

        quadro2 = new Quadro();
        quadro2.setId(quadro1.getId());
        quadro2.setNome(quadro1.getNome());
        quadro2.setOrganizacao(quadro1.getOrganizacao());
        quadro2.setUsuario(quadro1.getUsuario());
        quadro2.setProcessos(quadro1.getProcessos());
    }

    @Test
    void testEquals() {
        assertEquals(quadro1, quadro2);
    }

    @Test
    void testHashCode() {
        assertEquals(quadro1.hashCode(), quadro2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(quadro1.toString(), quadro2.toString());
    }

    @Test
    void testNotEquals() {
        quadro2.setId(2L);
        assertNotEquals(quadro1, quadro2);
    }
}
