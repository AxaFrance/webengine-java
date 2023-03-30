package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.api.ITestStepWebExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.ActionContext;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.global.Platform;
import fr.axa.automation.webengine.global.Settings;
import fr.axa.automation.webengine.helper.TestDataHelper;
import fr.axa.automation.webengine.report.object.ActionReportDetail;
import fr.axa.automation.webengine.util.CommonClassUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Slf4j
@Component
@Qualifier("testStepWebExecutor")
public class TestStepWebExecutor extends AbstractTestStepExecutor implements ITestStepWebExecutor {
    IActionExecutor actionExecutor;

    @Autowired
    public TestStepWebExecutor(IActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    public ActionReportDetail run(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, ITestStep testStep) throws WebEngineException {
        IAction action = getAction(globalApplicationContext, testCaseContext, testStep);
        return actionExecutor.run(action);
    }

    protected IAction getAction(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, ITestStep testStep) throws WebEngineException {
        Class<? extends IAction> clazz = getActionClass(globalApplicationContext,testStep);
        ActionContext actionContext = getActionContext(globalApplicationContext, testCaseContext);
        return CommonClassUtil.createAndCallMethod(clazz, "setActionDetailContext", actionContext);
    }

    protected Class<? extends IAction> getActionClass(AbstractGlobalApplicationContext globalApplicationContext, ITestStep testStep) {
        Class<? extends IAction> clazz = testStep.getAction();
        if(globalApplicationContext.getSettings().getPlatform()!= Platform.WINDOWS && testStep.getMobileAction()!=null){
            clazz = testStep.getMobileAction();
        }
        return clazz;
    }

    protected ActionContext getActionContext(AbstractGlobalApplicationContext globalAppContext, AbstractTestCaseContext testCaseContext){
        GlobalApplicationContext globalApplicationContext =  (GlobalApplicationContext)globalAppContext;
        List<TestData> testDataList = globalApplicationContext.getTestSuiteData().getTestDatas();
        Optional<TestData> testDataByTestCase = TestDataHelper.getDataOfTestCase(testDataList,testCaseContext.getTestCaseName());
        TestCaseAdditionalInformation testCaseAdditionalInformation = globalApplicationContext.getTestCaseAdditionnalInformationList().get(testCaseContext.getTestCaseName());

        return ActionContext.builder()
                .testCaseName(testCaseContext.getTestCaseName())
                .webDriver(testCaseContext.getWebDriver())
                .environmentVariables(globalApplicationContext.getEnvironmentVariables())
                .testCaseData(testDataByTestCase.orElse(null))
                .settings((Settings) globalApplicationContext.getSettings())
                .testCaseAdditionalInformation(testCaseAdditionalInformation).build();
    }
}
