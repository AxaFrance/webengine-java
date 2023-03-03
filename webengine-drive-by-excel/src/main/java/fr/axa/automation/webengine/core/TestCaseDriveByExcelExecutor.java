package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.api.ITestCaseDriveByExcelContext;
import fr.axa.automation.webengine.api.ITestCaseDriveByExcelExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.object.TestCaseData;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("testCaseDriveByExcelExecutor")
public class TestCaseDriveByExcelExecutor extends AbstractTestCaseWebExecutor implements ITestCaseDriveByExcelExecutor {

    @Autowired
    public TestCaseDriveByExcelExecutor(ITestStepExecutor testStepExecutor, GlobalConfigProperties globalConfigProperties, ILoggerService loggerService ) {
        super(testStepExecutor, globalConfigProperties, loggerService);
    }

    @Override
    public ITestCaseContext getTestCaseContext() throws WebEngineException {
        return TestCaseDriveByExcelContext.builder().build();
    }

    @Override
    public ITestCaseContext initialize(GlobalApplicationContext globalApplicationContext, TestCaseData testCaseData) throws WebEngineException {
        Object webDriver = initializeWebDriver(globalApplicationContext);
        return createTestCaseContext(testCaseData, webDriver);
    }

    protected ITestCaseContext createTestCaseContext(TestCaseData testCaseData, Object webDriver) throws WebEngineException {
        ITestCaseContext testCaseContext = super.createTestCaseContext(testCaseData.getName(),webDriver);
        ((ITestCaseDriveByExcelContext)testCaseContext).setTestCaseData(testCaseData);
        return testCaseContext;
    }

    @Override
    public TestCaseReport run(GlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext) throws WebEngineException {
//        String testCaseName = testCaseContext.getTestCaseName();
//        TestCaseReport testCaseReport = TestCaseReportHelper.createTestCaseReport(testCaseName);
//        List<ActionReport> actionReportList = new ArrayList<>();
//
//        try {
//            actionReportList.addAll(runTestStep(globalApplicationContext, testCaseContext));
//        }catch (Throwable e){
//            testCaseReport.setResult(Result.FAILED);
//            loggerService.error("Error during execution of test case : "+testCaseName,e);
//        }finally {
//            testCaseReport.getActionReports().getActionReports().addAll(ActionReportHelper.getActionReportList(actionReportList));
//            testCaseReport.setTestData(testDataByTestCase.map(TestData::getData).orElse(null));
//            testCaseReport.setEndTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
//            testCaseReport.setResult(getResultOfTestCase(actionReportList));          }
//        return testCaseReport;
        return null;
    }

}
