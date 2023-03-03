package fr.axa.automation.webengine.global;

import fr.axa.automation.webengine.api.ITestCase;
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
public abstract class AbstractTestCaseWebContext extends AbstractTestCaseContext {
    WebDriver webDriver;

    @Builder
    public AbstractTestCaseWebContext(String testCaseName, WebDriver webDriver) {
        super(testCaseName);
        this.webDriver = webDriver;
    }

    @Override
    public void setWebDriver(Object webDriver) {
        this.webDriver = (WebDriver)webDriver;
    }
}
