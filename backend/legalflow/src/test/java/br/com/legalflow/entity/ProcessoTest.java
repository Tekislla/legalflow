package br.com.legalflow.entity;

import br.com.legalflow.enums.ProcessoStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ProcessoTest {

    private Processo processo1;
    private Processo processo2;

    @BeforeEach
    void setUp() {
        processo1 = new Processo();
        processo1.setId(1L);
        processo1.setNome("Processo A");
        processo1.setNumero("0001");
        processo1.setAutor("Autor");
        processo1.setReu("Reu");
        processo1.setPrazoSubsidio(new Date());
        processo1.setPrazoFatal(new Date());
        processo1.setStatus(ProcessoStatusEnum.CRIADO.name());
        processo1.setDescricao("Descrição do processo A");
        processo1.setArquivo(new byte[0]);
        processo1.setQuadro(new Quadro());

        processo2 = new Processo();
        processo2.setId(processo1.getId());
        processo2.setNome(processo1.getNome());
        processo2.setNumero(processo1.getNumero());
        processo2.setAutor(processo1.getAutor());
        processo2.setReu(processo1.getReu());
        processo2.setPrazoSubsidio(processo1.getPrazoSubsidio());
        processo2.setPrazoFatal(processo1.getPrazoFatal());
        processo2.setStatus(processo1.getStatus());
        processo2.setDescricao(processo1.getDescricao());
        processo2.setArquivo(processo1.getArquivo());
        processo2.setQuadro(processo1.getQuadro());
    }

    @Test
    void testEquals() {
        assertEquals(processo1, processo2);
    }

    @Test
    void testHashCode() {
        assertEquals(processo1.hashCode(), processo2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(processo1.toString(), processo2.toString());
    }

    @Test
    void testNotEquals() {
        processo2.setId(2L);
        assertNotEquals(processo1, processo2);
    }
}
