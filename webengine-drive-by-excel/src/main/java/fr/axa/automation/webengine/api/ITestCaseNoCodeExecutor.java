package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestCaseExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.object.TestCaseNodeNoCode;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;

import java.util.List;

public interface ITestCaseNoCodeExecutor extends ITestCaseExecutor {
    AbstractTestCaseContext initialize(AbstractGlobalApplicationContext globalApplicationContext, TestSuiteDataNoCode testSuiteData, TestCaseNodeNoCode testCaseNodeToRun, String dataTestColumnName) throws WebEngineException;

    TestCaseReport run(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext) throws WebEngineException;

    default void cleanUp(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, List<CommandResult> commandResultList) throws WebEngineException{
    }

}
