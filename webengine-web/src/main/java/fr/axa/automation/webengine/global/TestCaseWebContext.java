package fr.axa.automation.webengine.global;

import fr.axa.automation.webengine.core.ITestCase;

import fr.axa.automation.webengine.api.ITestCaseWebContext;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.WebDriver;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TestCaseWebContext extends AbstractTestCaseWebContext implements ITestCaseWebContext {

    ITestCase testCaseToExecute;

    @Builder
    public TestCaseWebContext(String testCaseName, WebDriver webDriver, ITestCase testCaseToExecute) {
        super(testCaseName,webDriver);
        this.testCaseToExecute = testCaseToExecute;
    }

}
