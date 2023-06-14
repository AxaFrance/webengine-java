package fr.axa.automation.webengine.global;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum ExcelColumn {
    FIELD_NAME(0),COMMAND(1),TARGETS(2),OPTIONAL(3),DATA_TEST_REFERENCE(4);
    Integer value;
}
