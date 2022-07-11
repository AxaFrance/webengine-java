package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.ActionContext;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.report.ActionReportDetail;
import fr.axa.automation.webengine.util.ClassUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Slf4j
public abstract class AbstractTestStepExecutor implements ITestStepExecutor {

    IActionExecutor actionExecutor;

    public AbstractTestStepExecutor(IActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    public Object initialize(GlobalApplicationContext globalApplicationContext){
        return null;
    }

    public void cleanUp(Object object) {
    }

    public ActionReportDetail run(GlobalApplicationContext globalApplicationContext, Object context, String testCaseName, ITestStep testStep) throws WebEngineException {
        IAction action = getAction(globalApplicationContext, context, testCaseName, testStep);
        return actionExecutor.run(globalApplicationContext, action);
    }

    protected IAction getAction(GlobalApplicationContext globalApplicationContext, Object context, String testCaseName, ITestStep testStep) throws WebEngineException {
        IAction action;
        Class<? extends IAction> clazz ;
        try {
            clazz = testStep.getAction();
            ActionContext actionContext = getActionContext(globalApplicationContext, context, testCaseName);
            action = ClassUtil.createAndPopulateAction(clazz, "setActionDetailContext",actionContext);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new WebEngineException("Error during instantiation of Action : " + testStep.getAction() +" for this this test case :"+ testCaseName,e);
        }
        return action;
    }

    protected ActionContext getActionContext(GlobalApplicationContext globalApplicationContext, Object context, String testCaseName){
        List<TestData> testDataList = globalApplicationContext.getTestSuiteData().getTestData();
        Optional<TestData> testDataByTestCase = TestDataUtil.getDataOfTestCase(testDataList,testCaseName);
        TestCaseAdditionalInformation testCaseAdditionalInformation = globalApplicationContext.getTestCaseAdditionnalInformationList().get(testCaseName);

        return ActionContext.builder()
                .testCaseName(testCaseName)
                .context(context)
                .environmentVariables(globalApplicationContext.getEnvironmentVariables())
                .testCaseData(testDataByTestCase.orElse(null))
                .settings(globalApplicationContext.getSettings())
                .testCaseAdditionalInformation(testCaseAdditionalInformation).build();
    }
}
