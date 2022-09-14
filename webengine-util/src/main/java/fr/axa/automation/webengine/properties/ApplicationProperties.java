package fr.axa.automation.webengine.properties;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationProperties {
    String name;
    String platformName;
    String browserName;
}
