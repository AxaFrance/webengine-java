package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.general.ITestCaseContext;
import fr.axa.automation.webengine.general.TestCaseContext;
import fr.axa.automation.webengine.helper.GlobalConfigPropertiesHelper;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.util.BrowserFactory;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TestCaseExecutorWeb extends AbstractTestCaseExecutor {

    @Autowired
    public TestCaseExecutorWeb(ITestStepExecutor testStepExecutor, GlobalConfigProperties globalConfigProperties, ILoggerService loggerService ) {
        super(testStepExecutor, globalConfigProperties, loggerService);
    }

    @Override
    public ITestCaseContext initializeTestCaseContext(GlobalApplicationContext globalApplicationContext) throws WebEngineException {
        try {
            GlobalConfigProperties globalConfigProperties = GlobalConfigPropertiesHelper.getGlobalConfigProperties(globalApplicationContext.getSettings());
            Optional<WebDriver> optional = BrowserFactory.getDriver(globalConfigProperties);
            if(optional.isPresent()){
                return TestCaseContext.builder().webDriver(optional.get()).build();
            }
        } catch (Exception e) {
            throw new WebEngineException("Error during get driver",e);
        }
        return null;
    }

    @Override
    public void cleanUp(ITestCaseContext testCaseContext) {
        try {
            ((TestCaseContext)testCaseContext).getWebDriver().quit();
            loggerService.info("Browser close properly");
        }catch (NoSuchSessionException e){
            loggerService.warn("Warning during quit browser",e);
        }
    }
}
