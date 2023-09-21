package fr.axa.automation.webengine.constante;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum HtmlAttributeConstant {
    ATTRIBUTE_TYPE("type"),
    ATTRIBUTE_CHECKED("checked"),
    ATTRIBUTE_VALUE("value");

    final String value;
}
