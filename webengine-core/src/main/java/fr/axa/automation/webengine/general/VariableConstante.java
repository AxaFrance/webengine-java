package fr.axa.automation.webengine.general;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum VariableConstante {
    UNIQUE_ID("UNIQUE_ID");
    final String value;
}
