package fr.axa.automation.webengine.constante;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum InputType {
    ATTRIBUTE_TYPE_TEXT("text"),ATTRIBUTE_TYPE_RADIO("radio"), ATTRIBUTE_TYPE_CHECKBOX("checkbox"),ATTRIBUTE_TYPE_FILE("file");
    final String value;
}
