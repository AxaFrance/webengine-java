package fr.axa.automation.webengine.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum FormatDate {
    YYYYMMDD_HH("yyyyMMdd_hh"),
    YYYYMMDD_HHMMSS("yyyyMMdd_hhmmss");
    final String format;
}
