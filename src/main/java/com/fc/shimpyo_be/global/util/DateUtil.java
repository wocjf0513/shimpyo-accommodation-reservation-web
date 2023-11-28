package com.fc.shimpyo_be.global.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public final static String LOCAL_DATE_PATTERN = "yyyy-MM-dd";

    public final static String LOCAL_DATE_REGEX_PATTERN = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])";

    public static String toString(LocalDate dateObject) {
        return dateObject.format(DateTimeFormatter.ofPattern(LOCAL_DATE_PATTERN));
    }

    public static LocalDate toLocalDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(LOCAL_DATE_PATTERN));
    }
}