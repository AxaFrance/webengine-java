package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.generated.Variable;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.localtesting.LocalListenerRunner;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractTestSuiteExecutor implements ITestSuiteExecutor {

    LoggerService loggerService;
    ITestCaseExecutor testCaseExecutor;
    LocalListenerRunner localListenerRunner = LocalListenerRunner.getInstance();

    public AbstractTestSuiteExecutor(LoggerService loggerService, ITestCaseExecutor testCaseExecutor) {
        this.loggerService = loggerService;
        this.testCaseExecutor = testCaseExecutor;
    }

    public Object initialize(GlobalApplicationContext globalApplicationContext){
        localListenerRunner.started();
        return null;
    }

    public void cleanUp(Object object) {
        localListenerRunner.finished();
    }

    public TestSuiteReport run(GlobalApplicationContext globalApplicationContext, ITestSuite testSuite) throws WebEngineException, UnknownHostException {
        Calendar startTime = Calendar.getInstance();
        TestSuiteReport testSuiteReport = new TestSuiteReport();
        List<AbstractMap.SimpleEntry<String,? extends ITestCase>> testCaseList;
        List<TestCaseReport> testCaseReportList = new ArrayList<>();

        try {
            if (testSuite != null) {
                testCaseList = testSuite.getTestCaseList();
                testCaseReportList.addAll(runTestCase(globalApplicationContext, testCaseList));
            }
        }catch (WebEngineException e){
            testSuiteReport.setSystemError(ExceptionUtils.getStackTrace(e));
        }finally {
            testSuiteReport.setHostName(InetAddress.getLocalHost().getHostName());
            testSuiteReport.setStartTime(startTime);
            testSuiteReport.setEnvironmentVariables(globalApplicationContext.getEnvironmentVariables());
            testSuiteReport.getTestResult().addAll(testCaseReportList);
            testSuiteReport.setEndTime(Calendar.getInstance());
        }

        return testSuiteReport;
    }

    protected List<TestCaseReport> runTestCase(GlobalApplicationContext globalApplicationContext,List<AbstractMap.SimpleEntry<String,? extends ITestCase>> testCaseList) throws WebEngineException {
        if(CollectionUtils.isEmpty(testCaseList)){
            throw new WebEngineException("No Test case found in the project");
        }
        List<TestCaseReport> testCaseReportList = new ArrayList<>();
        TestCaseReport testCaseReport;
        for (AbstractMap.SimpleEntry<String,? extends ITestCase> entry: testCaseList) {
            if(canRunTestCase(entry.getKey(),globalApplicationContext)){
                testCaseReport = testCaseExecutor.run(globalApplicationContext,entry.getKey(),entry.getValue());
                testCaseReportList.add(testCaseReport);
            }
        }
        return testCaseReportList;
    }

    protected Boolean canRunTestCase(String testCaseName, GlobalApplicationContext globalApplicationContext) {
        boolean canRun = true;
        Map<String, TestCaseAdditionalInformation> testCaseAdditionalInformationMap = globalApplicationContext.getTestCaseAdditionnalInformationList();
        if (MapUtils.isNotEmpty(testCaseAdditionalInformationMap)) {
            TestCaseAdditionalInformation testCaseAdditionalInformation = testCaseAdditionalInformationMap.get(testCaseName);
            if (testCaseAdditionalInformation != null && !testCaseAdditionalInformation.isCanRun()) {
                canRun = false;
                List<Variable> missingDataList = globalApplicationContext.getTestCaseAdditionnalInformationList().get(testCaseName).getMissingDataList();
                for (Variable variable : missingDataList) {
                    loggerService.info("Can't run test case :" + testCaseName + " because, missing datas are found " + variable.getName());
                }
            }
        }

        return canRun;
    }

}
