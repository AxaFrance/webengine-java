package fr.axa.automation.webengine.core;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class HtmlAttribute {
    String name;
    String value;
}
