package fr.axa.automation.webengine.global;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@SuperBuilder
public class ElementContentSelect extends ElementContent{
    Map<String,String> valueAndTextMap;
}
