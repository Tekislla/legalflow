package br.com.legalflow.utils;

import br.com.legalflow.exception.generic.FormatarDataException;
import br.com.legalflow.exception.generic.InstanciaClasseFinalException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    private DateUtils() {
        throw new InstanciaClasseFinalException();
    }

    public static Date getDataFormatada(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            throw new FormatarDataException(dateStr);
        }
    }

    public static Date getDataSomandoDias(Integer diasASomar) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, diasASomar);
        return calendar.getTime();
    }
}
