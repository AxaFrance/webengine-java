package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.api.ITestCaseDriveByExcelExecutor;
import fr.axa.automation.webengine.api.ITestStepDriveByExcelExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseNodeDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.report.helper.TestCaseReportHelper;
import fr.axa.automation.webengine.tree.TreeNode;
import fr.axa.automation.webengine.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("testCaseDriveByExcelExecutor")
public class TestCaseDriveByExcelExecutor extends AbstractTestCaseWebExecutor implements ITestCaseDriveByExcelExecutor {

    @Autowired
    public TestCaseDriveByExcelExecutor(@Qualifier("testStepDriveByExcelExecutor") ITestStepExecutor testStepExecutor, GlobalConfigProperties globalConfigProperties, ILoggerService loggerService ) {
        super(testStepExecutor, globalConfigProperties, loggerService);
    }

    @Override
    public AbstractTestCaseContext getTestCaseContext() {
        return TestCaseDriveByExcelContext.builder().build();
    }

    @Override
    public AbstractTestCaseContext initialize(AbstractGlobalApplicationContext globalApplicationContext, TestCaseNodeDriveByExcel testCaseToRun, TestSuiteDataDriveByExcel testSuiteData) throws WebEngineException {
        Object webDriver = initializeWebDriver(globalApplicationContext);
        return createTestCaseContext(webDriver, testCaseToRun, testSuiteData);
    }

    protected AbstractTestCaseContext createTestCaseContext(Object webDriver, TestCaseNodeDriveByExcel testCaseToRun, TestSuiteDataDriveByExcel testSuiteData) throws WebEngineException {
        AbstractTestCaseContext testCaseContext = super.createTestCaseContext(webDriver,testCaseToRun.getName());
        ((TestCaseDriveByExcelContext)testCaseContext).setTestCaseToRun(testCaseToRun);
        ((TestCaseDriveByExcelContext)testCaseContext).setTestSuiteData(testSuiteData);
        return testCaseContext;
    }

    @Override
    public TestCaseReport run(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext) throws WebEngineException {
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

    protected List<ActionReport> runTestStep(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext) throws WebEngineException {
        ActionReport actionReport = new ActionReport();
        List<ActionReport> actionReportList = new ArrayList<>();

        TestCaseDriveByExcelContext testCaseDriveByExcelContext = (TestCaseDriveByExcelContext) testCaseContext;
        String testCaseName = testCaseDriveByExcelContext.getTestCaseName();
        TreeNode rootNode = testCaseDriveByExcelContext.getTestCaseToRun().getTreeNode();
        String commandName = "";
        boolean ignoredAllNextCmd = false;

        List<TreeNode> treeNodeCommandList = rootNode.getChildren();
        try {
            for (TreeNode treeNodeCommand : treeNodeCommandList){
                CommandDataDriveByExcel commandData = ((CommandDataDriveByExcel)treeNodeCommand.getData());
                commandName = commandData.getId();
                actionReport = new ActionReport();
                actionReport.setName(commandName);

                if(ignoredAllNextCmd){
                    actionReport.setResult(Result.IGNORED);
                    actionReportList.add(actionReport);
                    loggerService.info("All command are ignored. Test case is : "+ testCaseName +" and command name is : "+ commandName);
                }else{
                    actionReport = ((ITestStepDriveByExcelExecutor)testStepExecutor).run(globalApplicationContext,testCaseContext,commandData);
                    //if "call command"  call runTestStep again
                    //manage if, else if, else
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
