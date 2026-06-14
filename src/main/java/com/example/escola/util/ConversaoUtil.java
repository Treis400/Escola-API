package com.example.escola.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public final class ConversaoUtil {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private ConversaoUtil() {}

    public static LocalDate paraLocalDate(String dataTexto) {
        try {
            return LocalDate.parse(dataTexto, FORMATO_DATA);
        } catch (DateTimeParseException ex) {
            throw new DateTimeParseException(
                    "Data de nascimento invalida: '" + dataTexto + "'. Utilize o formato dd/MM/yyyy com uma data real.",
                    dataTexto, ex.getErrorIndex());
        }
    }

    public static String paraTextoData(LocalDate data) {
        return data.format(FORMATO_DATA);
    }

    public static BigDecimal paraBigDecimal(String notaTexto) {
        return new BigDecimal(notaTexto);
    }

    public static String paraTextoNota(BigDecimal nota) {
        BigDecimal semZeros = nota.stripTrailingZeros();
        if (semZeros.scale() < 1) {
            semZeros = semZeros.setScale(1);
        }
        return semZeros.toPlainString();
    }
}
