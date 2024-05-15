package fr.axa.automation.webengine.constante;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.File;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum AdditionalDataPath {
    ADDITIONAL_TEST_CASE_DATA_DIR("additional-data"),
    ADDITIONAL_TEST_CASE_DATA_FILE("test-case-data.yml");
    final String value;

    public static String getAdditionDataPath(){
        return AdditionalDataPath.ADDITIONAL_TEST_CASE_DATA_DIR.getValue() + File.separator + AdditionalDataPath.ADDITIONAL_TEST_CASE_DATA_FILE.getValue();
    }
}
