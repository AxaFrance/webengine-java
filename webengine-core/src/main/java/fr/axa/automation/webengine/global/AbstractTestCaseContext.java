package fr.axa.automation.webengine.global;


import fr.axa.automation.webengine.api.ITestCaseContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
public abstract class AbstractTestCaseContext implements ITestCaseContext {
    String testCaseName;
}
