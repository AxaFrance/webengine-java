package fr.axa.automation.webengine.global;

import fr.axa.automation.webengine.api.ITestCase;
import fr.axa.automation.webengine.api.ITestCaseDriveByExcelContext;
import fr.axa.automation.webengine.api.ITestCaseWebContext;
import fr.axa.automation.webengine.object.TestCaseData;
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
public class TestCaseDriveByExcelContext extends AbstractTestCaseWebContext implements ITestCaseDriveByExcelContext {
    TestCaseData testCaseData;

    @Builder
    public TestCaseDriveByExcelContext(String testCaseName, WebDriver webDriver, TestCaseData testCaseData) {
        super(testCaseName,webDriver);
        this.testCaseData = testCaseData;
    }

    @Override
    public void setTestCaseData(TestCaseData testCaseData) {
        this.testCaseData = testCaseData;
    }

    @Override
    public TestCaseData getTestCaseData() {
        return testCaseData;
    }
}
