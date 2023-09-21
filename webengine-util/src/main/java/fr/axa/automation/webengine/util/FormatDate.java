package fr.axa.automation.webengine.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum FormatDate {
    DDMMYYYY("dd/MM/yyyy"),

    DDMMYYYYHHMM("ddMMyyyyhhmm"),

    YYYYMMDD_HH("yyyyMMdd-HH"),
    YYYYMMDD_HHMMSS("yyyyMMdd-HHMMSS"),

    YYYYMMDD_T_HHMMSS("yyyy-MM-dd'T'HH:mm:ss");

    final String format;
}
