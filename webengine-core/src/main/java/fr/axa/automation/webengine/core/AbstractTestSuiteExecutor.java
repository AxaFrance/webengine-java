package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.general.ITestCaseContext;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.generated.Variable;
import fr.axa.automation.webengine.localtesting.ILocalTestingRunner;
import fr.axa.automation.webengine.logger.ILoggerService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractTestSuiteExecutor implements ITestSuiteExecutor {

    ITestCaseExecutor testCaseExecutor;
    ILocalTestingRunner localTestingRunner;
    
    ILoggerService loggerService;

    protected AbstractTestSuiteExecutor(ITestCaseExecutor testCaseExecutor, ILocalTestingRunner localTestingRunner, ILoggerService loggerService) {
        this.testCaseExecutor = testCaseExecutor;
        this.localTestingRunner = localTestingRunner;
        this.loggerService = loggerService;
    }

    public Object initialize(GlobalApplicationContext globalApplicationContext) {
        runLocalTesting(globalApplicationContext);
        return null;
    }

    private void runLocalTesting(GlobalApplicationContext globalApplicationContext) {
        localTestingRunner.startLocalTesting();
    }

    public void cleanUp(Object object) {
        stopLocalTesting();
    }

    private void stopLocalTesting() {
        localTestingRunner.stopLocalTesting();
    }

    public TestSuiteReport run(GlobalApplicationContext globalApplicationContext, ITestSuite testSuite) throws WebEngineException, UnknownHostException {
        Calendar startTime = Calendar.getInstance();
        TestSuiteReport testSuiteReport = new TestSuiteReport();
        List<AbstractMap.SimpleEntry<String, ? extends ITestCase>> testCaseList;
        List<TestCaseReport> testCaseReportList = new ArrayList<>();

        try {
            if (testSuite != null) {
                testCaseList = testSuite.getTestCaseList();
                testCaseReportList.addAll(runTestCase(globalApplicationContext, testCaseList));
            }
        } catch (WebEngineException e) {
            testSuiteReport.setSystemError(ExceptionUtils.getStackTrace(e));
        } finally {
            testSuiteReport.setHostName(InetAddress.getLocalHost().getHostName());
            testSuiteReport.setStartTime(startTime);
            testSuiteReport.setEnvironmentVariables(globalApplicationContext.getEnvironmentVariables());
            testSuiteReport.getTestResults().addAll(testCaseReportList);
            testSuiteReport.setEndTime(Calendar.getInstance());
        }

        return testSuiteReport;
    }

    protected List<TestCaseReport> runTestCase(GlobalApplicationContext globalApplicationContext, List<AbstractMap.SimpleEntry<String, ? extends ITestCase>> testCaseList) throws WebEngineException {
        if (CollectionUtils.isEmpty(testCaseList)) {
            throw new WebEngineException("No Test case found in the project");
        }
        List<TestCaseReport> testCaseReportList = new ArrayList<>();
        for (AbstractMap.SimpleEntry<String, ? extends ITestCase> entry : testCaseList) {
            String testCaseName = entry.getKey();
            ITestCase testCase = entry.getValue();
            if (isCanRunTestCase(testCaseName, globalApplicationContext)) {
                ITestCaseContext testCaseContext = testCaseExecutor.initialize(globalApplicationContext,testCaseName,testCase);
                TestCaseReport testCaseReport = testCaseExecutor.run(globalApplicationContext, testCaseContext);
                testCaseExecutor.cleanUp(testCaseContext);
                testCaseReportList.add(testCaseReport);
            }
        }
        return testCaseReportList;
    }

    protected boolean isCanRunTestCase(String testCaseName, GlobalApplicationContext globalApplicationContext) {
        Map<String, TestCaseAdditionalInformation> testCaseAdditionalInformationMap = globalApplicationContext.getTestCaseAdditionnalInformationList();
        if (MapUtils.isNotEmpty(testCaseAdditionalInformationMap)) {
            TestCaseAdditionalInformation testCaseAdditionalInformation = testCaseAdditionalInformationMap.get(testCaseName);
            if (testCaseAdditionalInformation != null && !testCaseAdditionalInformation.isCanRun()) {
                List<Variable> missingDataList = globalApplicationContext.getTestCaseAdditionnalInformationList().get(testCaseName).getMissingDataList();
                missingDataList.stream().forEach(variable -> loggerService.info("Can't run test case :" + testCaseName + " because, missing datas value are found " + variable.getName()));
                return false;
            }
        }
        return true;
    }
}
