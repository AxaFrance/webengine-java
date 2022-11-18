package fr.axa.automation.webengine.general;

import fr.axa.automation.webengine.core.ITestCase;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.WebDriver;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @Setter
@ToString
@EqualsAndHashCode
public class TestCaseContext extends GlobalTestCaseContext {
    WebDriver webDriver;

    @Builder
    public TestCaseContext(String testCaseName, ITestCase testCaseToExecute, WebDriver webDriver) {
        super(testCaseName,testCaseToExecute);
        this.webDriver = webDriver;
    }
}
