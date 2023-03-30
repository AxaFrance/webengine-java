package fr.axa.automation.webengine.global;

import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseNodeDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@SuperBuilder
public class TestCaseDriveByExcelContext extends AbstractTestCaseContext {
    TestCaseNodeDriveByExcel testCaseToRun;
    TestSuiteDataDriveByExcel testSuiteData;
}
