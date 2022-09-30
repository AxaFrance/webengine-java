package fr.axa.automation.webengine.properties;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CapabilitiesProperties {
  Map<String,String> desiredCapabilitiesMap;
}
