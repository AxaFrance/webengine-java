package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.global.GlobalApplicationContext;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractTestSuite implements ITestSuite {
    GlobalApplicationContext globalApplicationContext;
}
