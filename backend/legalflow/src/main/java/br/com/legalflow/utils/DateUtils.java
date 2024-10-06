package br.com.legalflow.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    private DateUtils() {
        throw new UnsupportedOperationException("Essa classe serve apenas para utilidade, não deve ser instanciada");
    }

    public static Date getDataFormatada(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Unable to parse the date: " + dateStr, e);
        }
    }

    public static Date getDataSomandoDias(Integer diasASomar) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, diasASomar);
        return calendar.getTime();
    }
}
