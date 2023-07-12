package fr.axa.automation.webengine.executor;

import fr.axa.automation.webengine.api.ITestCaseNoCodeExecutor;
import fr.axa.automation.webengine.api.ITestStepNoCodeExecutor;
import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.constante.TargetKey;
import fr.axa.automation.webengine.core.AbstractTestCaseWebExecutor;
import fr.axa.automation.webengine.core.ITestStepExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.ArrayOfActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.CommandDataHelper;
import fr.axa.automation.webengine.helper.CommandNameHelper;
import fr.axa.automation.webengine.helper.CommandResultHelper;
import fr.axa.automation.webengine.helper.TestCaseHelperNoCode;
import fr.axa.automation.webengine.helper.TestSuiteHelperNoCode;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.object.TestCaseDataNoCode;
import fr.axa.automation.webengine.object.TestCaseNodeNoCode;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.report.helper.TestCaseReportHelper;
import fr.axa.automation.webengine.tree.TreeNode;
import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.StringUtil;
import fr.axa.automation.webengine.util.UriUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Qualifier("testCaseNoCodeExecutor")
public class TestCaseNoCodeExecutor extends AbstractTestCaseWebExecutor implements ITestCaseNoCodeExecutor {

    @Autowired
    public TestCaseNoCodeExecutor(@Qualifier("testStepNoCodeExecutor") ITestStepExecutor testStepExecutor, GlobalConfiguration globalConfiguration, ILoggerService loggerService) {
        super(testStepExecutor, globalConfiguration, loggerService);
    }

    @Override
    public AbstractTestCaseContext getTestCaseContext() {
        return TestCaseNoCodeContext.builder().build();
    }

    @Override
    public AbstractTestCaseContext initialize(AbstractGlobalApplicationContext globalApplicationContext, TestSuiteDataNoCode testSuiteData, TestCaseNodeNoCode testCaseNodeToRun, String dataTestColumnName) throws WebEngineException {
        Map<CommandDataNoCode, WebDriver> webDriverByApplicationVisitedList = getWebDriverByApplicationVisited(globalApplicationContext, testSuiteData, testCaseNodeToRun);
        return createTestCaseContext(webDriverByApplicationVisitedList, testSuiteData, testCaseNodeToRun, dataTestColumnName);
    }

    private Map<CommandDataNoCode, WebDriver> getWebDriverByApplicationVisited(AbstractGlobalApplicationContext globalApplicationContext, TestSuiteDataNoCode testSuiteData, TestCaseNodeNoCode testCaseNodeToRun) throws WebEngineException {
        TestCaseDataNoCode testCaseDataNoCode = TestSuiteHelperNoCode.getTestCaseDataNoCode(testSuiteData, testCaseNodeToRun.getName());
        List<CommandDataNoCode> applicationVisitedList = TestSuiteHelperNoCode.getApplicationVisited(testCaseDataNoCode);
        Map<CommandDataNoCode, WebDriver> webDriverByApplicationVisitedList = new LinkedHashMap();
        for (CommandDataNoCode commandDataNoCode : applicationVisitedList) {
            if (MapUtils.isNotEmpty(webDriverByApplicationVisitedList)) {
                List<CommandDataNoCode> commandDataListWithSameApplicationVisitedList = webDriverByApplicationVisitedList.keySet().stream().filter(cmdData -> isSameUrl(commandDataNoCode, cmdData)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(commandDataListWithSameApplicationVisitedList)) {
                    webDriverByApplicationVisitedList.put(commandDataNoCode, webDriverByApplicationVisitedList.get(commandDataListWithSameApplicationVisitedList.get(0)));
                    continue;
                }
            }
            webDriverByApplicationVisitedList.put(commandDataNoCode, (WebDriver) initializeWebDriver(globalApplicationContext));
        }
        return webDriverByApplicationVisitedList;
    }

    private static boolean isSameUrl(CommandDataNoCode commandDataNoCode, CommandDataNoCode cmdData)  {
        try {
            return StringUtil.equalsIgnoreCase(UriUtil.getHostFromURI(cmdData.getTargetList().get(TargetKey.OPEN)), UriUtil.getHostFromURI(commandDataNoCode.getTargetList().get(TargetKey.OPEN)));
        } catch (WebEngineException e) {
            return false;
        }
    }

    public Object initializeWebDriver(AbstractGlobalApplicationContext globalApplicationContext) throws WebEngineException {
        return super.initializeWebDriver(globalApplicationContext);
    }

    @Override
    public void cleanUp(AbstractGlobalApplicationContext globalApplicationContext, Object testCaseContext) {
        try {
            if(globalApplicationContext.getSettings().isCloseBrowser()){
                List<WebDriver> webDriverList = ((TestCaseNoCodeContext)testCaseContext).getDriverByCommandData().values().stream().distinct().collect(Collectors.toList());
                webDriverList.stream().forEach(webDriver -> {
                    if (webDriver != null) {
                        webDriver.quit();
                    }
                });
                loggerService.info("Browser close properly");
            }

        }catch (NoSuchSessionException e){
            loggerService.warn("Warning during quit browser",e);
        }
    }

    protected AbstractTestCaseContext createTestCaseContext(Map<CommandDataNoCode, WebDriver> webDriverByUrlList, TestSuiteDataNoCode testSuiteData, TestCaseNodeNoCode testCaseToRun, String dataTestColumnName) throws WebEngineException {
        AbstractTestCaseContext testCaseContext = getTestCaseContext();
        testCaseContext.setTestCaseName(testCaseToRun.getName());
        TestCaseDataNoCode testCaseDataNoCode = TestSuiteHelperNoCode.getTestCaseDataNoCode(testSuiteData, testCaseToRun.getName());
        Map<String, WebDriver> driverByCommandData = new LinkedHashMap<>();
        WebDriver currentWebDriver = webDriverByUrlList.entrySet().stream().findFirst().get().getValue();
        for (CommandDataNoCode commandDataNoCode : testCaseDataNoCode.getCommandList()) {
            if (commandDataNoCode.getCommand() == CommandName.OPEN) {
                currentWebDriver = webDriverByUrlList.get(commandDataNoCode);
            }
            driverByCommandData.put(commandDataNoCode.getUid(), currentWebDriver);
        }
        ((TestCaseNoCodeContext) testCaseContext).setDriverByCommandData(driverByCommandData);
        ((TestCaseNoCodeContext) testCaseContext).setTestSuiteData(testSuiteData);
        ((TestCaseNoCodeContext) testCaseContext).setTestCaseToRun(testCaseToRun);
        ((TestCaseNoCodeContext) testCaseContext).setDataTestColumnName(dataTestColumnName);
        return testCaseContext;
    }

    @Override
    public TestCaseReport run(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext) throws WebEngineException {
        TestCaseNoCodeContext testCaseNoCodeContext = (TestCaseNoCodeContext) testCaseContext;
        String testCaseName = testCaseNoCodeContext.getTestCaseName();
        TestCaseReport testCaseReport = TestCaseReportHelper.createTestCaseReport(TestCaseHelperNoCode.getReportTestCaseName(testCaseContext));
        List<CommandResult> commandResultList = new ArrayList<>();

        try {
            commandResultList.addAll(runTestStep(globalApplicationContext, testCaseNoCodeContext));
        } catch (Throwable e) {
            testCaseReport.setResult(Result.FAILED);
            loggerService.error("Error during execution of test case : " + testCaseName, e);
        } finally {
            List<ActionReport> actionReportList = CommandResultHelper.getActionReportList(commandResultList);
            testCaseReport.getActionReports().getActionReports().addAll(actionReportList);
            testCaseReport.setResult(getResultOfTestCase(CommandResultHelper.getActionReportList(filterCommandResult(commandResultList))));
            testCaseReport.setEndTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
            //testCaseReport.setTestData(testDataByTestCase.map(TestData::getData).orElse(null));
        }
        return testCaseReport;
    }

    protected List<CommandResult> runTestStep(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext) throws WebEngineException {
        TestCaseNoCodeContext testCaseNoCodeContext = (TestCaseNoCodeContext) testCaseContext;
        TreeNode rootNode = testCaseNoCodeContext.getTestCaseToRun().getTreeNode();
        return runTestStep(globalApplicationContext, testCaseContext, rootNode);
    }

    protected List<CommandResult> runTestStep(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, TreeNode treeNode) throws WebEngineException {
        ITestStepNoCodeExecutor stepExecutor = ((ITestStepNoCodeExecutor) testStepExecutor);
        CommandDataNoCode commandData = null;
        ActionReport actionReport = new ActionReport();
        CommandResult commandResult;
        boolean isSubReport;
        List<CommandResult> commandResultList = new ArrayList<>();
        List<CommandResult> commandResultOfSubCommandList = new ArrayList<>();
        Deque<Map<CommandName, Result>> nestedIfList = new LinkedList<>();

        String commandName = "";
        boolean ignoredAllNextCmd = false;

        TestCaseNoCodeContext testCaseNoCodeContext = (TestCaseNoCodeContext) testCaseContext;
        String testCaseName = testCaseNoCodeContext.getTestCaseName();
        String dataTestColumName = testCaseNoCodeContext.getDataTestColumnName();

        List<TreeNode> treeNodeCommandList = treeNode.getChildren();
        try {
            for (TreeNode treeNodeCommand : treeNodeCommandList) {
                isSubReport = false;
                commandData = ((CommandDataNoCode) treeNodeCommand.getData());
                commandName = CommandNameHelper.getCommandName(commandData);
                actionReport = new ActionReport();
                actionReport.setName(commandName);
                actionReport.setId(UUID.randomUUID().toString());

                if (ignoredAllNextCmd) {
                    actionReport.setResult(Result.IGNORED);
                    commandResultList.add(CommandResultHelper.getCommandResult(commandData, actionReport, ""));
                    loggerService.info("All command are ignored. Test case is : " + testCaseName + " and command name is : " + commandName);
                    continue;
                }

                switch (commandData.getCommand()) {
                    case IF:
                        commandResult = stepExecutor.run(globalApplicationContext, testCaseContext, commandData, commandResultList);
                        nestedIfList.addLast(getResultOfCommand(CommandName.IF, commandResult.getActionReport().getResult()));
                        commandResultOfSubCommandList = CommandResultHelper.isResultExpected(commandResult, Result.PASSED) ? runTestStep(globalApplicationContext, testCaseContext, treeNodeCommand) : ignoreCommand(treeNodeCommand);
                        isSubReport = true;
                        break;
                    case ELSE_IF:
                    case ELSE:
                        Map<CommandName, Result> map = nestedIfList.getLast();
                        if (canExecute(map)) {
                            commandResult = stepExecutor.run(globalApplicationContext, testCaseContext, commandData, commandResultList);
                            if (CommandResultHelper.isResultExpected(commandResult, Result.PASSED)) {
                                commandResultOfSubCommandList = runTestStep(globalApplicationContext, testCaseContext, treeNodeCommand);
                                isSubReport = true;
                            }
                            map.put(commandData.getCommand(), commandResult.getActionReport().getResult());
                        } else {
                            commandResult = CommandResultHelper.getCommandResult(commandData, ActionReportHelper.getActionReport(commandName, Result.IGNORED), "");
                            commandResultOfSubCommandList = ignoreCommand(treeNodeCommand);
                            isSubReport = true;
                        }
                        break;
                    case END_IF:
                        commandResult = CommandResultHelper.getCommandResult(commandData, ActionReportHelper.getActionReport(commandData.getName(), Result.PASSED), "");
                        nestedIfList.removeLast();
                        break;
                    case CALL:
                        if (CommandDataHelper.canExecuteDataTestColumn(commandData.getDataTestReferenceList(), dataTestColumName)) {
                            commandResult = CommandResultHelper.getCommandResult(commandData, ActionReportHelper.getActionReport(commandData.getName(), Result.PASSED), "");
                            commandResultOfSubCommandList = runTestStep(globalApplicationContext, TestCaseHelperNoCode.getTestCaseContext(testCaseContext, commandData.getTargetList().get(TargetKey.CALL)));
                            List<ActionReport> actionReportCallList = CommandResultHelper.getActionReportList(commandResultOfSubCommandList);
                            commandResult.getActionReport().setResult(getResultOfTestCase(actionReportCallList));
                            isSubReport = true;
                        } else {
                            actionReport = ActionReportHelper.getActionReport(commandData.getName(), Result.IGNORED);
                            actionReport.setLog("Command ignored because the colum data-test-ref not contains '" + dataTestColumName + "' column");
                            commandResult = CommandResultHelper.getCommandResult(commandData, actionReport, "");
                        }
                        break;
                    default:
                        commandResult = stepExecutor.run(globalApplicationContext, testCaseContext, commandData, commandResultList);
                        if (commandData.isOptional() && CommandResultHelper.isResultExpected(commandResult, Result.PASSED) && CollectionUtils.isNotEmpty(treeNodeCommand.getChildren())) {
                            commandResultOfSubCommandList = runTestStep(globalApplicationContext, testCaseContext, treeNodeCommand);
                            isSubReport = true;
                        } else if (commandData.isOptional() && (CommandResultHelper.isResultExpected(commandResult, Result.IGNORED) || CommandResultHelper.isResultExpected(commandResult, Result.FAILED) || CommandResultHelper.isResultExpected(commandResult, Result.CRITICAL_ERROR))) {
                            commandResultOfSubCommandList = ignoreCommand(treeNodeCommand);
                            isSubReport = true;
                        }
                        break;
                }
                commandResultList.add(commandResult);
                if (isSubReport && CollectionUtils.isNotEmpty(commandResultOfSubCommandList)) {
                    commandResult.getActionReport().setSubActionReports(new ArrayOfActionReport());
                    commandResult.getActionReport().getSubActionReports().getActionReports().addAll(CommandResultHelper.getActionReportList(commandResultOfSubCommandList));
                }
                ignoredAllNextCmd = isIgnoredAllOtherAction(commandResult);
            }
        } catch (Throwable e) {
            loggerService.info("Fatal exception during command : " + commandName + " and test case name is : " + testCaseName + ". All commands are cancelled.");
            actionReport.setResult(Result.CRITICAL_ERROR);
            actionReport.setLog(ExceptionUtils.getStackTrace(e));
            commandResultList.add(CommandResultHelper.getCommandResult(commandData, actionReport, ""));
        }
        return commandResultList;
    }

    protected List<CommandResult> ignoreCommand(TreeNode treeNodeCommand) {
        List<CommandResult> actionReportList = new ArrayList<>();
        List<TreeNode> treeNodeCommandChildrenList = treeNodeCommand.getChildren();
        for (TreeNode treeNodeChildren : treeNodeCommandChildrenList) {
            CommandDataNoCode commandData = ((CommandDataNoCode) treeNodeChildren.getData());
            String commandName = CommandNameHelper.getCommandName(commandData);
            ActionReport actionReport = ActionReportHelper.getActionReport(commandName, Result.IGNORED);
            actionReportList.add(CommandResultHelper.getCommandResult(commandData, actionReport, ""));
            if (commandData.isOptional() && CollectionUtils.isNotEmpty(treeNodeChildren.getChildren())) {
                actionReportList.addAll(ignoreCommand(treeNodeChildren));
            }
        }
        return actionReportList;
    }

    protected Map<CommandName, Result> getResultOfCommand(CommandName commandName, Result result) {
        Map<CommandName, Result> map = new HashMap<>();
        map.put(commandName, result);
        return map;
    }

    protected boolean canExecute(Map<CommandName, Result> map) {
        if (map.containsValue(Result.PASSED)) {
            return false;
        }
        return true;
    }

    protected boolean isIgnoredAllOtherAction(CommandResult commandResult) {
        boolean ignored = false;
        if (commandResult.getCommandData().getCommand() != CommandName.IF && commandResult.getCommandData().getCommand() != CommandName.ELSE_IF) {
            return isIgnoredAllOtherAction(commandResult.getActionReport());
        }
        return ignored;
    }

    protected List<CommandResult> filterCommandResult(List<CommandResult> commandResultList) {
        return commandResultList.stream().filter(commandResult -> CommandName.IF != commandResult.getCommandData().getCommand() && CommandName.ELSE_IF != commandResult.getCommandData().getCommand()).collect(Collectors.toList());
    }
}
