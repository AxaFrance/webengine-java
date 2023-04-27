package fr.axa.automation.webengine.constante;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum Constante {
    TRUE("true");
    final String value;
}
