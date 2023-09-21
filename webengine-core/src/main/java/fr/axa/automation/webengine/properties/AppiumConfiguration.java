package fr.axa.automation.webengine.properties;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AppiumConfiguration {
    String gridConnection;
    String userName;
    String password;
    LocalTestingConfiguration localTesting;
    AppiumCapabilities capabilities;
}
