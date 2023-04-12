package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.api.ITestCaseDriveByExcelExecutor;
import fr.axa.automation.webengine.api.ITestStepDriveByExcelExecutor;
import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.ArrayOfActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.CommandNameHelper;
import fr.axa.automation.webengine.helper.CommandResultHelper;
import fr.axa.automation.webengine.helper.TestCaseHelperDriveByExcel;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.object.TestCaseNodeDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.report.helper.TestCaseReportHelper;
import fr.axa.automation.webengine.tree.TreeNode;
import fr.axa.automation.webengine.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;
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

    public Object initializeWebDriver(AbstractGlobalApplicationContext globalApplicationContext) throws WebEngineException {
        Object webDriver = super.initializeWebDriver(globalApplicationContext);
        ((WebDriver)webDriver).manage().window().maximize();
        return webDriver;
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
        TestCaseDriveByExcelContext testCaseDriveByExcelContext = (TestCaseDriveByExcelContext)testCaseContext;
        String testCaseName = testCaseDriveByExcelContext.getTestCaseName();
        TestCaseReport testCaseReport = TestCaseReportHelper.createTestCaseReport(testCaseName+"-"+testCaseDriveByExcelContext.getDataTestColumnName());
        List<CommandResult> commandResultList = new ArrayList<>();

        try {
            commandResultList.addAll(runTestStep(globalApplicationContext, testCaseDriveByExcelContext));
        } catch (Throwable e) {
            testCaseReport.setResult(Result.FAILED);
            loggerService.error("Error during execution of test case : " + testCaseName, e);
        } finally {
            List<ActionReport> actionReportList = CommandResultHelper.getActionReportList(commandResultList);
            testCaseReport.getActionReports().getActionReports().addAll(actionReportList);
            testCaseReport.setEndTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
            testCaseReport.setResult(getResultOfTestCase(actionReportList));
            //testCaseReport.setTestData(testDataByTestCase.map(TestData::getData).orElse(null));
        }
        return testCaseReport;
    }

    protected List<CommandResult> runTestStep(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext) throws WebEngineException {
        TestCaseDriveByExcelContext testCaseDriveByExcelContext = (TestCaseDriveByExcelContext) testCaseContext;
        TreeNode rootNode = testCaseDriveByExcelContext.getTestCaseToRun().getTreeNode();
        return runTestStep(globalApplicationContext, testCaseContext, rootNode);
    }

    private List<CommandResult> runTestStep(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, TreeNode treeNode) throws WebEngineException {
        ITestStepDriveByExcelExecutor stepExecutor = ((ITestStepDriveByExcelExecutor)testStepExecutor);
        CommandDataDriveByExcel commandData = null;
        ActionReport actionReport = new ActionReport();
        CommandResult commandResult = null;
        boolean isSubReport = false;
        List<CommandResult> commandResultList = new ArrayList<>();
        List<CommandResult> commandResultOfSubCommandList = new ArrayList<>();
        Deque<Map<CommandName,Result>> nestedIfList = new LinkedList<>();

        String commandName = "";
        boolean ignoredAllNextCmd = false;

        TestCaseDriveByExcelContext testCaseDriveByExcelContext = (TestCaseDriveByExcelContext) testCaseContext;
        String testCaseName = testCaseDriveByExcelContext.getTestCaseName();

        List<TreeNode> treeNodeCommandList = treeNode.getChildren();
        try {
            for (TreeNode treeNodeCommand : treeNodeCommandList){
                isSubReport = false;
                commandData = ((CommandDataDriveByExcel)treeNodeCommand.getData());
                commandName = CommandNameHelper.getCommandName(commandData);
                actionReport = new ActionReport();
                actionReport.setName(commandName);

                if(ignoredAllNextCmd){
                    actionReport.setResult(Result.IGNORED);
                    commandResultList.add(CommandResultHelper.getCommandResult(commandData,actionReport,""));
                    loggerService.info("All command are ignored. Test case is : "+ testCaseName +" and command name is : "+ commandName);
                    continue;
                }

                switch (commandData.getCommand()){
                    case IF:
                        commandResult = stepExecutor.run(globalApplicationContext, testCaseContext,commandData,commandResultList);
                        nestedIfList.addLast(getResultOfCommand(CommandName.IF,commandResult.getActionReport().getResult()));
                        if(CommandResultHelper.isResultExpected(commandResult,Result.PASSED)){
                            commandResultOfSubCommandList = runTestStep(globalApplicationContext,testCaseContext,treeNodeCommand);
                        }else{
                            commandResultOfSubCommandList = ignoreCommand(treeNodeCommand);
                        }
                        isSubReport = true;
                        break;
                    case ELSE_IF:
                    case ELSE:
                        Map<CommandName,Result> map = nestedIfList.getLast();
                        if(canExecute(map)){
                            commandResult = stepExecutor.run(globalApplicationContext, testCaseContext,commandData,commandResultList);
                            if(CommandResultHelper.isResultExpected(commandResult,Result.PASSED)){
                                commandResultOfSubCommandList = runTestStep(globalApplicationContext,testCaseContext,treeNodeCommand);
                                isSubReport = true;
                            }
                            map.put(commandData.getCommand(),actionReport.getResult());
                        }else{
                            commandResult = CommandResultHelper.getCommandResult(commandData,ActionReportHelper.getActionReport(commandName,Result.IGNORED),"");
                            commandResultOfSubCommandList = ignoreCommand(treeNodeCommand);
                            isSubReport = true;
                        }
                        break;
                    case END_IF:
                        commandResult = CommandResultHelper.getCommandResult(commandData,ActionReportHelper.getActionReport(commandData.getName(),Result.PASSED),"");
                        nestedIfList.removeLast();
                        break;
                    case CALL:
                        commandResult = CommandResultHelper.getCommandResult(commandData,ActionReportHelper.getActionReport(commandData.getName(),Result.PASSED),"");
                        commandResultOfSubCommandList = runTestStep(globalApplicationContext, TestCaseHelperDriveByExcel.getTestCaseContext(testCaseContext,commandData.getTargetList().get(CommandName.CALL.getName())));
                        isSubReport = true;
                        break;
                    default:
                        commandResult = stepExecutor.run(globalApplicationContext, testCaseContext,commandData,commandResultList);
                        if(commandData.isOptional() && CommandResultHelper.isResultExpected(commandResult,Result.PASSED) && CollectionUtils.isNotEmpty(treeNodeCommand.getChildren())){
                            commandResultOfSubCommandList = runTestStep(globalApplicationContext,testCaseContext,treeNodeCommand);
                            isSubReport = true;
                        }
                        break;
                }
                commandResultList.add(commandResult);
                if(isSubReport){
                    commandResult.getActionReport().setSubActionReports(new ArrayOfActionReport());
                    commandResult.getActionReport().getSubActionReports().getActionReports().addAll(CommandResultHelper.getActionReportList(commandResultOfSubCommandList));
                }
                ignoredAllNextCmd = isIgnoredAllOtherAction(commandResult.getActionReport());
            }
        }catch (Throwable e){
            loggerService.info("Fatal exception during command : "+ commandName +" and test case name is : "+ testCaseName +". All commands are cancelled.");
            actionReport.setResult(Result.CRITICAL_ERROR);
            actionReport.setLog(ExceptionUtils.getStackTrace(e));
            commandResultList.add(CommandResultHelper.getCommandResult(commandData,actionReport,""));
        }
        return commandResultList;
    }

    private List<CommandResult> ignoreCommand(TreeNode treeNodeCommand) {
        List<CommandResult> actionReportList = new ArrayList<>();
        List<TreeNode> treeNodeCommandChildrenList = treeNodeCommand.getChildren();
        for (TreeNode treeNodeChildren:treeNodeCommandChildrenList) {
            CommandDataDriveByExcel commandData = ((CommandDataDriveByExcel)treeNodeChildren.getData());
            String commandName = CommandNameHelper.getCommandName(commandData);
            ActionReport actionReport = ActionReportHelper.getActionReport(commandName,Result.IGNORED);
            actionReportList.add(CommandResultHelper.getCommandResult(commandData,actionReport,""));
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
