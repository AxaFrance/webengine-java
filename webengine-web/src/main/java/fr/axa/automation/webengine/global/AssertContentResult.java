package fr.axa.automation.webengine.global;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@SuperBuilder
public class AssertContentResult {
    String expectedValue;
    String actualValue;
    boolean result;
}
