package fr.axa.automation.webengine.global;

import fr.axa.automation.webengine.object.TestCaseNodeDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@SuperBuilder
public class TestCaseDriveByExcelContext extends AbstractTestCaseContext {
    TestCaseNodeDriveByExcel testCaseToRun;
    String dataTestColumnName;
    TestSuiteDataDriveByExcel testSuiteData;
}
