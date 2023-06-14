package fr.axa.automation.webengine.constante;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum LocatingBy {
    BY_ID("id"),BY_NAME("name"), BY_CLASS_NAME("className"),
    BY_LINK_TEXT("linkText"),BY_TAG_NAME("tagName"),BY_CSS_SELECTOR("cssSelector"),
    BY_XPATH("xPath"),BY_ATTRIBUTE_LIST("attributeList"),
    BY_COMBINAISON_OF_LOCATOR("combinaisonOfLocator");
    final String value;
}
