package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractTestSuite implements ITestSuite {
    AbstractGlobalApplicationContext globalApplicationContext;
}
