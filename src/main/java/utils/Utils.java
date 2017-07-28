package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Utils {
    public static DateTimeFormatter formatterOfMMM_yyyy = DateTimeFormatter.ofPattern("MMM_yyyy", new Locale("ru"));
    public static DateTimeFormatter formatterOfyyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatDateToMMM_yyyyString(LocalDate date) {
        return formatterOfMMM_yyyy.format(date);
    }

    public static String formatDateToyyyyMMdd(LocalDate date) {
        return formatterOfyyyyMMdd.format(date);
    }
}
