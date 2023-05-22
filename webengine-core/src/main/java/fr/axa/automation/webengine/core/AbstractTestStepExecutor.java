package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.ActionContext;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.general.ITestCaseContext;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.helper.TestDataHelper;
import fr.axa.automation.webengine.report.object.ActionReportDetail;
import fr.axa.automation.webengine.util.CommonClassUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Slf4j
public abstract class AbstractTestStepExecutor implements ITestStepExecutor {

    IActionExecutor actionExecutor;

    protected AbstractTestStepExecutor(IActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    public Object initialize(GlobalApplicationContext globalApplicationContext){
        return null;
    }

    public void cleanUp(Object object) {
    }

    public ActionReportDetail run(GlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext, ITestStep testStep) throws WebEngineException {
        IAction action = getAction(globalApplicationContext, testCaseContext, testStep);
        return actionExecutor.run(action);
    }

    protected IAction getAction(GlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext, ITestStep testStep) throws WebEngineException {
        Class<? extends IAction> clazz = getActionClass(globalApplicationContext,testStep);
        ActionContext actionContext = getActionContext(globalApplicationContext, testCaseContext);
        return CommonClassUtil.createAndCallMethod(clazz, "setActionDetailContext", actionContext);
    }

    protected Class<? extends IAction> getActionClass(GlobalApplicationContext globalApplicationContext, ITestStep testStep) {
        Class<? extends IAction> clazz = testStep.getAction();
        if(globalApplicationContext.getSettings().getPlatform()!= Platform.WINDOWS && testStep.getMobileAction()!=null){
            clazz = testStep.getMobileAction();
        }
        return clazz;
    }

    protected ActionContext getActionContext(GlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext){
        List<TestData> testDataList = globalApplicationContext.getTestSuiteData().getTestDatas();
        Optional<TestData> testDataByTestCase = TestDataHelper.getDataOfTestCase(testDataList,testCaseContext.getTestCaseName());
        TestCaseAdditionalInformation testCaseAdditionalInformation = globalApplicationContext.getTestCaseAdditionnalInformationList().get(testCaseContext.getTestCaseName());

        return ActionContext.builder()
                .testCaseName(testCaseContext.getTestCaseName())
                .webDriver(testCaseContext.getWebDriver())
                .environmentVariables(globalApplicationContext.getEnvironmentVariables())
                .testCaseData(testDataByTestCase.orElse(null))
                .settings(globalApplicationContext.getSettings())
                .testCaseAdditionalInformation(testCaseAdditionalInformation).build();
    }
}
