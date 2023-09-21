package fr.axa.automation.webengine.constante;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum OptionalConstante {
    OPTIONAL("optional"),
    OPTIONAL_AND_DEPENDS_ON_PREVIOUS("optional and depends on previous");
    final String value;
}
