package fr.axa.automation.webengine.executor;

import fr.axa.automation.webengine.core.AbstractTestSuiteExecutor;
import fr.axa.automation.webengine.core.ITestCaseExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.GlobalApplicationContextNoCode;
import fr.axa.automation.webengine.global.SettingsNoCode;
import fr.axa.automation.webengine.helper.TestCaseHelperNoCode;
import fr.axa.automation.webengine.localtesting.ILocalTestingRunner;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.object.TestCaseDataNoCode;
import fr.axa.automation.webengine.object.TestCaseNodeNoCode;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
import fr.axa.automation.webengine.report.helper.TestSuiteReportHelper;
import fr.axa.automation.webengine.report.object.TestSuiteReportInformation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("testSuiteNoCodeExecutor")
public class TestSuiteNoCodeExecutor extends AbstractTestSuiteExecutor implements ITestSuiteNoCodeExecutor {

    @Autowired
    public TestSuiteNoCodeExecutor(@Qualifier("testCaseNoCodeExecutor") ITestCaseExecutor testCaseExecutor, ILocalTestingRunner localTestingRunner, ILoggerService loggerService) {
        super(testCaseExecutor, localTestingRunner, loggerService);
    }

    @Override
    public TestSuiteReport run(AbstractGlobalApplicationContext globalAppContext, TestSuiteDataNoCode testSuiteData) throws WebEngineException, UnknownHostException {
        loggerService.info("Start run test suite ");
        GlobalApplicationContextNoCode globalApplicationContext = (GlobalApplicationContextNoCode) globalAppContext;
        Calendar startTime = Calendar.getInstance();
        TestSuiteReport testSuiteReport;
        List<TestCaseReport> testCaseReportList = new ArrayList<>();
        String systemError = "";

        try {
            testCaseReportList.addAll(runTestCase(globalApplicationContext, testSuiteData));
        } catch (WebEngineException e) {
            systemError = ExceptionUtils.getStackTrace(e);
        } finally {
            TestSuiteReportInformation testSuiteReportInformation = TestSuiteReportInformation.builder().environmentVariables(null).testCaseReportList(testCaseReportList).startTime(startTime).systemError(systemError).build();
            testSuiteReport = TestSuiteReportHelper.getTestSuiteReport(testSuiteReportInformation);
        }
        loggerService.info("End run test suite");
        return testSuiteReport;
    }

    protected List<TestCaseReport> runTestCase(AbstractGlobalApplicationContext globalApplicationContext, TestSuiteDataNoCode testSuiteData) throws WebEngineException {
        List<TestCaseDataNoCode> testCaseDataList = testSuiteData.getTestCaseList();
        if (CollectionUtils.isEmpty(testCaseDataList)) {
            throw new WebEngineException("No Test case found in the file");
        }
        List<TestCaseReport> testCaseReportList = new ArrayList<>();
        SettingsNoCode settingsNoCode = (SettingsNoCode) globalApplicationContext.getSettings();
        List<TestCaseNodeNoCode> testCaseToRunList = getTestCaseListToRun(testSuiteData, settingsNoCode);

        for (TestCaseNodeNoCode testCaseNodeToRun : testCaseToRunList) {
            List<String> dataTestColumNameList = getDataTestColumNameList(settingsNoCode, testCaseNodeToRun);
            for (String dataTestColumnName : dataTestColumNameList) {
                AbstractTestCaseContext testCaseContext = ((ITestCaseNoCodeExecutor) testCaseExecutor).initialize(globalApplicationContext, testSuiteData, testCaseNodeToRun, dataTestColumnName );
                testCaseReportList.add(testCaseExecutor.run(globalApplicationContext, testCaseContext));
            }
        }
        return testCaseReportList;
    }

    private List<String> getDataTestColumNameList(SettingsNoCode settingsNoCode, TestCaseNodeNoCode testCaseNodeToRun) {
        List<String> dataTestColumNameList = settingsNoCode.getTestCaseAndDataTestColumName().get(testCaseNodeToRun.getName());
        if(CollectionUtils.isEmpty(dataTestColumNameList)){
            dataTestColumNameList = TestCaseHelperNoCode.getDataTestColumnName(testCaseNodeToRun);
        }
        return dataTestColumNameList;
    }

    private List<TestCaseNodeNoCode> getTestCaseListToRun(TestSuiteDataNoCode testSuiteData, SettingsNoCode settingsNoCode) {
        List<TestCaseNodeNoCode> testCaseNodeList = testSuiteData.getTestCaseNodeList();
        List<String> nameOfTestCaseToRun = new ArrayList<>(settingsNoCode.getTestCaseAndDataTestColumName().keySet());
        return testCaseNodeList.stream().filter(testCaseNodeNoCode -> nameOfTestCaseToRun.contains(testCaseNodeNoCode.getName())).collect(Collectors.toList());
    }


    public void deleteTempFile(String dataTestFileName) {
        loggerService.info("Start deleting temp file ");
        try {
            Files.deleteIfExists(Paths.get(dataTestFileName));
        } catch (Exception e) {
            loggerService.error("Error when deleting temp file : " + dataTestFileName, e);
        }
        loggerService.info("End deleting temp file ");
    }
}
