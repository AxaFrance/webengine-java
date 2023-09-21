package fr.axa.automation.webengine.global;

import fr.axa.automation.webengine.core.ITestCase;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@SuperBuilder
public class TestCaseWebContext extends AbstractTestCaseContext  {
    ITestCase testCaseToExecute;
}
