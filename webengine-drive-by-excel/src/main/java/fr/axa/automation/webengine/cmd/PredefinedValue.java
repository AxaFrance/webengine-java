package fr.axa.automation.webengine.cmd;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum PredefinedValue {
    TODAY("TODAY");
    final String value;
}
