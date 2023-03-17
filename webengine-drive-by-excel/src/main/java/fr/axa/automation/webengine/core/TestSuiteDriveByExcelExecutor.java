package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.api.ITestCaseDriveByExcelExecutor;
import fr.axa.automation.webengine.api.ITestSuiteDriveByExcelExecutor;
import fr.axa.automation.webengine.checking.chain.IChecking;
import fr.axa.automation.webengine.checking.chain.impl.AbstractChecking;
import fr.axa.automation.webengine.checking.chain.impl.CallScenariiChecking;
import fr.axa.automation.webengine.checking.chain.impl.DataTestReferenceChecking;
import fr.axa.automation.webengine.checking.chain.impl.IfChecking;
import fr.axa.automation.webengine.checking.chain.impl.ReferencedValueChecking;
import fr.axa.automation.webengine.checking.chain.impl.TestCaseEndingChecking;
import fr.axa.automation.webengine.checking.runner.ICheckingRunner;
import fr.axa.automation.webengine.checking.runner.impl.CheckingRunner;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.GlobalApplicationContextDriveByExcel;
import fr.axa.automation.webengine.localtesting.ILocalTestingRunner;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.object.AbstractTestSuiteDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import fr.axa.automation.webengine.report.helper.TestSuiteReportHelper;
import fr.axa.automation.webengine.report.object.TestSuiteReportInformation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
@Qualifier("testSuiteDriveByExcelExecutor")
public class TestSuiteDriveByExcelExecutor extends AbstractTestSuiteExecutor implements ITestSuiteDriveByExcelExecutor {

    @Autowired
    public TestSuiteDriveByExcelExecutor(@Qualifier("testCaseDriveByExcelExecutor")ITestCaseExecutor testCaseExecutor, ILocalTestingRunner localTestingRunner, ILoggerService loggerService) {
        super(testCaseExecutor,localTestingRunner, loggerService);
    }

    @Override
    public TestSuiteReport run(AbstractGlobalApplicationContext globalAppContext, AbstractTestSuiteDataDriveByExcel testSuiteData) throws WebEngineException, UnknownHostException {
        GlobalApplicationContextDriveByExcel globalApplicationContext = (GlobalApplicationContextDriveByExcel) globalAppContext;
        Calendar startTime = Calendar.getInstance();
        TestSuiteReport testSuiteReport;
        List<TestCaseReport> testCaseReportList = new ArrayList<>();
        String systemError = "";
        checkInput(testSuiteData);
        try {
            if (testSuiteData != null) {
                List<TestCaseDataDriveByExcel> testCaseDataList = ((TestSuiteDataDriveByExcel)testSuiteData).getTestCaseList();
                testCaseReportList.addAll(runTestCaseData(globalApplicationContext, testCaseDataList));
            }
        } catch (WebEngineException e) {
            systemError = ExceptionUtils.getStackTrace(e);
        } finally {
            TestSuiteReportInformation testSuiteReportInformation = TestSuiteReportInformation.builder().environmentVariables(null).testCaseReportList(testCaseReportList).startTime(startTime).systemError(systemError).build();
            testSuiteReport = TestSuiteReportHelper.getTestSuiteReport(testSuiteReportInformation);
        }
        return testSuiteReport;
    }

    protected List<TestCaseReport> runTestCaseData(AbstractGlobalApplicationContext globalApplicationContext, List<TestCaseDataDriveByExcel> testCaseDataList) throws WebEngineException {
        if (CollectionUtils.isEmpty(testCaseDataList)) {
            throw new WebEngineException("No Test case found in the file");
        }
        List<TestCaseReport> testCaseReportList = new ArrayList<>();
        for (TestCaseDataDriveByExcel testCaseData : testCaseDataList) {
            ITestCaseContext testCaseContext = ((ITestCaseDriveByExcelExecutor)testCaseExecutor).initialize(globalApplicationContext,testCaseData);
            TestCaseReport testCaseReport = testCaseExecutor.run(globalApplicationContext, testCaseContext);
            testCaseExecutor.cleanUp(testCaseContext);
            testCaseReportList.add(testCaseReport);
        }
        return testCaseReportList;
    }

    public void checkInput(AbstractTestSuiteDataDriveByExcel testSuiteData){
        IChecking checking = AbstractChecking.link(
                new TestCaseEndingChecking(),
                new IfChecking(),
                new CallScenariiChecking(),
                new DataTestReferenceChecking(),
                new ReferencedValueChecking()
        );
        ICheckingRunner checkingRunner = new CheckingRunner();
        checkingRunner.setChecking(checking);
        checkingRunner.runChecking(testSuiteData);
    }
}
