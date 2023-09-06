package fr.axa.automation.webengine.global;


import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@SuperBuilder
public class SettingsNoCode extends AbstractSettings {
    String dataTestFileName;
    Map<String, List<String>> testCaseAndDataTestColumName;
    String keePassDatabasePath;
    @ToString.Exclude
    String keePassDatabasePassword;
}
