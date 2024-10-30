package br.com.legalflow.utils;

import br.com.legalflow.exception.generic.FormatarDataException;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;

public class DateUtilsTest {

    @Test
    public void shouldThrowFormatarDataException() {
        assertThrows(FormatarDataException.class, () -> {
            DateUtils.getDataFormatada("2020-01-01", "dd/MM/yyyy");
        });
    }
}
