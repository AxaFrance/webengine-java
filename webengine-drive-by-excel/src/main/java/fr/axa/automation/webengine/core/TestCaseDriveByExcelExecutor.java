package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.api.ITestCaseDriveByExcelExecutor;
import fr.axa.automation.webengine.api.ITestStepDriveByExcelExecutor;
import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.TestCaseHelperDriveByExcel;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseNodeDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.report.helper.TestCaseReportHelper;
import fr.axa.automation.webengine.tree.TreeNode;
import fr.axa.automation.webengine.util.DateUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    public AbstractTestCaseContext initialize(AbstractGlobalApplicationContext globalApplicationContext, TestSuiteDataDriveByExcel testSuiteData, TestCaseNodeDriveByExcel testCaseToRun, String dataTestColmnName ) throws WebEngineException {
        Object webDriver = initializeWebDriver(globalApplicationContext);
        return createTestCaseContext(webDriver, testSuiteData, testCaseToRun,dataTestColmnName);
    }

    protected AbstractTestCaseContext createTestCaseContext(Object webDriver, TestSuiteDataDriveByExcel testSuiteData, TestCaseNodeDriveByExcel testCaseToRun, String dataTestColmnName ) throws WebEngineException {
        AbstractTestCaseContext testCaseContext = super.createTestCaseContext(webDriver,testCaseToRun.getName());
        ((TestCaseDriveByExcelContext)testCaseContext).setTestSuiteData(testSuiteData);
        ((TestCaseDriveByExcelContext)testCaseContext).setTestCaseToRun(testCaseToRun);
        ((TestCaseDriveByExcelContext)testCaseContext).setDataTestColumnName(dataTestColmnName);
        return testCaseContext;
    }

    @Override
    public TestCaseReport run(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext) throws WebEngineException {
        String testCaseName = testCaseContext.getTestCaseName();
        TestCaseReport testCaseReport = TestCaseReportHelper.createTestCaseReport(testCaseName);
        List<ActionReport> actionReportList = new ArrayList<>();

        try {
            actionReportList.addAll(runTestStep(globalApplicationContext, testCaseContext));
        } catch (Throwable e) {
            testCaseReport.setResult(Result.FAILED);
            loggerService.error("Error during execution of test case : " + testCaseName, e);
        } finally {
            testCaseReport.getActionReports().getActionReports().addAll(actionReportList);
//            testCaseReport.setTestData(testDataByTestCase.map(TestData::getData).orElse(null));
            testCaseReport.setEndTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
            testCaseReport.setResult(getResultOfTestCase(actionReportList));
        }
        return testCaseReport;
    }

    protected List<ActionReport> runTestStep(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext) throws WebEngineException {
        TestCaseDriveByExcelContext testCaseDriveByExcelContext = (TestCaseDriveByExcelContext) testCaseContext;
        TreeNode rootNode = testCaseDriveByExcelContext.getTestCaseToRun().getTreeNode();
        return runTestStep(globalApplicationContext, testCaseContext, rootNode);
    }

    private List<ActionReport> runTestStep(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, TreeNode treeNode) throws WebEngineException {
        ActionReport actionReport = new ActionReport();
        List<ActionReport> actionReportList = new ArrayList<>();
        Deque<Map<CommandName,Result>> nestedIfList = new LinkedList<>();

        String commandName = "";
        boolean ignoredAllNextCmd = false;

        TestCaseDriveByExcelContext testCaseDriveByExcelContext = (TestCaseDriveByExcelContext) testCaseContext;
        String testCaseName = testCaseDriveByExcelContext.getTestCaseName();

        List<TreeNode> treeNodeCommandList = treeNode.getChildren();
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
                    continue;
                }

                switch (CommandName.fromValue(commandData.getCommand())){
                    case IF:
                        actionReport = ((ITestStepDriveByExcelExecutor)testStepExecutor).run(globalApplicationContext, testCaseContext,commandData);
                        actionReportList.add(actionReport);
                        if(actionReport.getResult()==Result.PASSED){
                            actionReportList.addAll(runTestStep(globalApplicationContext,testCaseContext,treeNodeCommand));
                        }else{
                            actionReportList.addAll(ignoreCommand(treeNodeCommand));
                        }
                        nestedIfList.addLast(getResultOfCommand(CommandName.IF,actionReport.getResult()));
                        break;
                    case ELSE_IF:
                    case ELSE:
                        Map<CommandName,Result> map = nestedIfList.getLast();
                        if(canExecute(map)){
                            actionReport = ((ITestStepDriveByExcelExecutor)testStepExecutor).run(globalApplicationContext, testCaseContext,commandData);
                            actionReportList.add(actionReport);
                            if(actionReport.getResult()==Result.PASSED){
                                actionReportList.addAll(runTestStep(globalApplicationContext,testCaseContext,treeNodeCommand));
                            }
                            map.put(CommandName.valueOf(commandData.getCommand()),actionReport.getResult());
                        }else{
                            actionReportList.add(ActionReportHelper.getActionReport(commandName,Result.IGNORED));
                            actionReportList.addAll(ignoreCommand(treeNodeCommand));
                        }
                        break;

                    case END_IF:
                        actionReportList.add(ActionReportHelper.getActionReport(commandName));
                        nestedIfList.removeLast();
                        break;
                    case CALL:
                            actionReportList.addAll(runTestStep(globalApplicationContext, TestCaseHelperDriveByExcel.getTestCaseContext(testCaseContext,commandData.getTargetList().get(CommandName.CALL))));
                        break;
                    default:
                        actionReport = ((ITestStepDriveByExcelExecutor)testStepExecutor).run(globalApplicationContext, testCaseContext,commandData);
                        actionReportList.add(actionReport);
                        if(commandData.isOptional() && (actionReport.getResult()==Result.PASSED)){
                            actionReportList.addAll(runTestStep(globalApplicationContext,testCaseContext,treeNodeCommand));
                        }
                        break;
                }
                ignoredAllNextCmd = isIgnoredAllOtherAction(actionReport);
            }
        }catch (Throwable e){
            loggerService.info("Fatal exception during command : "+ commandName +" and test case name is : "+ testCaseName +". All commands are cancelled.");
            actionReport.setResult(Result.CRITICAL_ERROR);
            actionReport.setLog(ExceptionUtils.getStackTrace(e));
            actionReportList.add(actionReport);
        }
        return actionReportList;
    }

    private static List<ActionReport> ignoreCommand(TreeNode treeNodeCommand) {
        List<ActionReport> actionReportList = new ArrayList<>();
        List<TreeNode> treeNodeCommandChildrenList = treeNodeCommand.getChildren();
        for (TreeNode treeNodeChildren:treeNodeCommandChildrenList) {
            actionReportList.add(ActionReportHelper.getActionReport(((CommandDataDriveByExcel)treeNodeChildren.getData()).getId(),Result.IGNORED));
        }
        return actionReportList;
    }

    private Map<CommandName,Result> getResultOfCommand(CommandName commandName,Result result) {
        Map<CommandName,Result> map = new HashMap<>();
        map.put(commandName,result);
        return map;
    }

    private boolean canExecute(Map<CommandName,Result> map) {
        if(map.containsValue(Result.PASSED)){
            return false;
        }
        return true;
    }
}
