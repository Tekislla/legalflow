package br.com.legalflow.utils;

import br.com.legalflow.exception.generic.FormatarDataException;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;

class DateUtilsTest {

    @Test
    void shouldThrowFormatarDataException() {
        assertThrows(FormatarDataException.class, () -> {
            DateUtils.getDataFormatada("2020-01-01", "dd/MM/yyyy");
        });
    }
}
