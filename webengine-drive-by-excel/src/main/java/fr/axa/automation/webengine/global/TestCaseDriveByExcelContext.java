package fr.axa.automation.webengine.global;

import fr.axa.automation.webengine.api.ITestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.WebDriver;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class TestCaseDriveByExcelContext extends AbstractTestCaseContext implements ITestCaseDriveByExcelContext {
    TestCaseDataDriveByExcel testCaseData;

    @Builder
    public TestCaseDriveByExcelContext(String testCaseName, WebDriver webDriver, TestCaseDataDriveByExcel testCaseData) {
        super(testCaseName,webDriver);
        this.testCaseData = testCaseData;
    }

    @Override
    public void setTestCaseData(TestCaseDataDriveByExcel testCaseData) {
        this.testCaseData = testCaseData;
    }

    @Override
    public TestCaseDataDriveByExcel getTestCaseData() {
        return testCaseData;
    }
}
