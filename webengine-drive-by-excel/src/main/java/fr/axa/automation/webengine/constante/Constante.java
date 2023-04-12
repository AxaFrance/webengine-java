package fr.axa.automation.webengine.constante;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum Constante {
    TRIPLE_CHEVRON_PREFIX("<<<"),
    TRIPLE_CHEVRON_SUFFIX(">>>"),
    SEMICOLON(";"),
    OPTIONAL("optional"),

    EXCLAMATION_MARK("!"),

    MINUS("-"),
    PLUS("+"),
    OPTIONAL_AND_DEPENDS_ON_PREVIOUS("optional and depends on previous"),
    CR_LF ("\r\n");
    final String value;
}
