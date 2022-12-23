package fr.axa.automation.webengine.properties;

import fr.axa.automation.webengine.general.Browser;
import fr.axa.automation.webengine.general.Platform;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationProperties {
    String name;
    Platform platform;
    Browser browser;
    String outputDir;
}
