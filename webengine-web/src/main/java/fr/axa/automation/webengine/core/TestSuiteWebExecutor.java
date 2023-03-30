package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.api.ITestCaseWebExecutor;
import fr.axa.automation.webengine.api.ITestSuiteWebExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.generated.Variable;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.global.Settings;
import fr.axa.automation.webengine.localtesting.ILocalTestingRunner;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.report.helper.TestSuiteReportHelper;
import fr.axa.automation.webengine.report.object.TestSuiteReportInformation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("testSuiteWebExecutor")
public class TestSuiteWebExecutor extends AbstractTestSuiteExecutor implements ITestSuiteWebExecutor {

    @Autowired
    public TestSuiteWebExecutor(@Qualifier("testCaseWebExecutor") ITestCaseExecutor testCaseExecutor, ILocalTestingRunner localTestingRunner, ILoggerService loggerService) {
        super(testCaseExecutor,localTestingRunner, loggerService);
    }

    public TestSuiteReport run(AbstractGlobalApplicationContext globalApplicationContext, ITestSuite testSuite) throws WebEngineException, UnknownHostException {
        Calendar startTime = Calendar.getInstance();
        TestSuiteReport testSuiteReport;
        List<TestCaseReport> testCaseReportList = new ArrayList<>();
        String systemError = "";

        try {
            if (testSuite != null) {
                List<AbstractMap.SimpleEntry<String, ? extends ITestCase>> testCaseList = testSuite.getTestCaseList();
                testCaseReportList.addAll(runTestCase(globalApplicationContext, testCaseList));
            }
        } catch (WebEngineException e) {
            systemError = ExceptionUtils.getStackTrace(e);
        } finally {
            TestSuiteReportInformation testSuiteReportInformation = TestSuiteReportInformation.builder().environmentVariables(((GlobalApplicationContext)globalApplicationContext).getEnvironmentVariables()).testCaseReportList(testCaseReportList).startTime(startTime).systemError(systemError).build();
            testSuiteReport = TestSuiteReportHelper.getTestSuiteReport(testSuiteReportInformation);
        }

        return testSuiteReport;
    }

    protected List<TestCaseReport> runTestCase(AbstractGlobalApplicationContext globalAppContext, List<AbstractMap.SimpleEntry<String, ? extends ITestCase>> testCaseList) throws WebEngineException {
        GlobalApplicationContext globalApplicationContext = (GlobalApplicationContext) globalAppContext;
        if (CollectionUtils.isEmpty(testCaseList)) {
            throw new WebEngineException("No Test case found in the project");
        }
        List<TestCaseReport> testCaseReportList = new ArrayList<>();
        for (AbstractMap.SimpleEntry<String, ? extends ITestCase> entry : testCaseList) {
            String testCaseName = entry.getKey();
            ITestCase testCase = entry.getValue();
            if (isCanRunTestCase(testCaseName, globalApplicationContext) && isTestCaseExistInTestData(testCaseName, globalApplicationContext) && isTestCaseDefineInCommandLine(testCaseName,globalApplicationContext)) {
                AbstractTestCaseContext testCaseContext = ((ITestCaseWebExecutor)testCaseExecutor).initialize(globalApplicationContext,testCaseName,testCase);
                TestCaseReport testCaseReport = testCaseExecutor.run(globalApplicationContext, testCaseContext);
                testCaseExecutor.cleanUp(testCaseContext);
                testCaseReportList.add(testCaseReport);
            }
        }
        return testCaseReportList;
    }

    protected boolean isCanRunTestCase(String testCaseName, AbstractGlobalApplicationContext globalAppContext) {
        GlobalApplicationContext globalApplicationContext = (GlobalApplicationContext) globalAppContext;
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

    protected boolean isTestCaseExistInTestData(String testCaseName, AbstractGlobalApplicationContext globalAppContext){
        GlobalApplicationContext globalApplicationContext = (GlobalApplicationContext) globalAppContext;
        List<TestData> testDataList = globalApplicationContext.getTestSuiteData().getTestDatas();
        boolean findTestCaseInTestData = false;
        for (TestData testData : testDataList) {
            if(testCaseName.equalsIgnoreCase(testData.getTestName())){
                findTestCaseInTestData = true;
            }
        }
        if(!findTestCaseInTestData){
            loggerService.warn("Can't run test case :'" + testCaseName + "' because, it doesn't exist in testData");
        }
        return findTestCaseInTestData;
    }

    protected boolean isTestCaseDefineInCommandLine(String testCaseName, GlobalApplicationContext globalApplicationContext){
        List<String> testCaseToRunList = ((Settings)globalApplicationContext.getSettings()).getTestCaseToRunList();
        if(CollectionUtils.isEmpty(testCaseToRunList)){
            return true;
        }
        boolean findTestCaseToRun = false;
        for (String testCaseNameToRun : testCaseToRunList) {
            if(testCaseName.equalsIgnoreCase(testCaseNameToRun)){
                findTestCaseToRun = true;
            }
        }

        if(!findTestCaseToRun){
            loggerService.warn("Can't run test case :'" + testCaseName + "' because, it doesn't exist in arguments");
        }
        return findTestCaseToRun;
    }
}
