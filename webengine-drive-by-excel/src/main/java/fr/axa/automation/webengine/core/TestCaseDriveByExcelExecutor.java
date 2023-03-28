package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.api.ITestCaseDriveByExcelContext;
import fr.axa.automation.webengine.api.ITestCaseDriveByExcelExecutor;
import fr.axa.automation.webengine.api.ITestStepDriveByExcelExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.report.helper.TestCaseReportHelper;
import fr.axa.automation.webengine.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Qualifier("testCaseDriveByExcelExecutor")
public class TestCaseDriveByExcelExecutor extends AbstractTestCaseWebExecutor implements ITestCaseDriveByExcelExecutor {

    @Autowired
    public TestCaseDriveByExcelExecutor(@Qualifier("testStepDriveByExcelExecutor") ITestStepExecutor testStepExecutor, GlobalConfigProperties globalConfigProperties, ILoggerService loggerService ) {
        super(testStepExecutor, globalConfigProperties, loggerService);
    }

    @Override
    public ITestCaseContext getTestCaseContext() {
        return TestCaseDriveByExcelContext.builder().build();
    }

    @Override
    public ITestCaseContext initialize(AbstractGlobalApplicationContext globalApplicationContext, TestCaseDataDriveByExcel testCaseData) throws WebEngineException {
        Object webDriver = initializeWebDriver(globalApplicationContext);
        return createTestCaseContext(testCaseData, webDriver);
    }

    protected ITestCaseContext createTestCaseContext(TestCaseDataDriveByExcel testCaseData, Object webDriver) throws WebEngineException {
        ITestCaseContext testCaseContext = super.createTestCaseContext(testCaseData.getName(),webDriver);
        ((ITestCaseDriveByExcelContext)testCaseContext).setTestCaseData(testCaseData);
        return testCaseContext;
    }

    @Override
    public TestCaseReport run(AbstractGlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext) throws WebEngineException {
        String testCaseName = testCaseContext.getTestCaseName();
        TestCaseReport testCaseReport = TestCaseReportHelper.createTestCaseReport(testCaseName);
        List<ActionReport> actionReportList = new ArrayList<>();

        try {
            actionReportList.addAll(runTestStep(globalApplicationContext, testCaseContext));
        }catch (Throwable e){
            testCaseReport.setResult(Result.FAILED);
            loggerService.error("Error during execution of test case : "+testCaseName,e);
        }finally {
            testCaseReport.getActionReports().getActionReports().addAll(actionReportList);
//            testCaseReport.setTestData(testDataByTestCase.map(TestData::getData).orElse(null));
            testCaseReport.setEndTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
            testCaseReport.setResult(getResultOfTestCase(actionReportList));          }
        return testCaseReport;
    }

    protected List<ActionReport> runTestStep(AbstractGlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext) throws WebEngineException {
        ITestCaseDriveByExcelContext testCaseDriveByExcelContext = (ITestCaseDriveByExcelContext) testCaseContext;
        String testCaseName = testCaseDriveByExcelContext.getTestCaseName();
        ActionReport actionReport = new ActionReport();
        List<ActionReport> actionReportList = new ArrayList<>();
        List<CommandDataDriveByExcel> commandDataList = testCaseDriveByExcelContext.getTestCaseData().getCommandList();
        String commandName = "";
        boolean ignoredAllNextCmd = false;

        if(CollectionUtils.isEmpty(commandDataList)){
            throw new WebEngineException("No command found for this test case :"+testCaseName);
        }

        try {
            for (CommandDataDriveByExcel commandData : commandDataList){
                commandName = commandData.getId();
                actionReport = new ActionReport();
                actionReport.setName(commandName);

                if(ignoredAllNextCmd){
                    actionReport.setResult(Result.IGNORED);
                    actionReportList.add(actionReport);
                    loggerService.info("All command are ignored. Test case is : "+ testCaseName +" and command name is : "+ commandName);
                }else{
                    actionReport = ((ITestStepDriveByExcelExecutor)testStepExecutor).run(globalApplicationContext,testCaseContext,commandData);
                    actionReportList.add(actionReport);
                    ignoredAllNextCmd = isIgnoredAllOtherAction(actionReport);
                }
            }
        }catch (Throwable e){
            loggerService.info("Fatal exception during command : "+ commandName +" and test case name is : "+ testCaseName +". All commands are cancelled.");
            actionReport.setResult(Result.CRITICAL_ERROR);
            actionReport.setLog(ExceptionUtils.getStackTrace(e));
            actionReportList.add(actionReport);
        }
        return actionReportList;
    }
}
