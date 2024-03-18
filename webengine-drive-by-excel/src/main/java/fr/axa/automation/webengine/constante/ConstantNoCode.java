package fr.axa.automation.webengine.constante;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum ConstantNoCode {
    EXTERNAL_PREFIX("#{"),
    EXTERNAL_SUFFIX("}#"),
    INTERNAL_PREFIX("<<<"),
    INTERNAL_SUFFIX(">>>"),

    KEYBOARD_VALUE_PREFIX("${"),
    KEYBOARD_VALUE_SUFFIX("}"),
    SEMICOLON(";"),
    EXCLAMATION_MARK("!"),
    MINUS("-"),
    PLUS("+"),
    DASH("-"),
    CR_LF("\r\n"),
    DOUBLE_CR_LF("\r\n \r\n");
    final String value;
}
