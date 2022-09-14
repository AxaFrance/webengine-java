package fr.axa.automation.webengine.properties;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AppiumSettingsProperties {
    String gridConnection;
    String userName;
    String password;
    CapabilitiesProperties capabilities;
}
