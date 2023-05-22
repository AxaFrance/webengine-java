package fr.axa.automation.webengine.general;


import fr.axa.automation.webengine.core.ITestCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
public abstract class GlobalTestCaseContext implements ITestCaseContext {
    String testCaseName;
    ITestCase testCaseToExecute;
}
