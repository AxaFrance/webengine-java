package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.global.TestCaseWebContext;
import fr.axa.automation.webengine.helper.GlobalConfigPropertiesHelper;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.util.BrowserFactory;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

public abstract class AbstractTestCaseWebExecutor extends AbstractTestCaseExecutor {

    public AbstractTestCaseWebExecutor(ITestStepExecutor testStepExecutor, GlobalConfigProperties globalConfigProperties, ILoggerService loggerService ) {
        super(testStepExecutor, globalConfigProperties, loggerService);
    }

    @Override
    public Object initializeWebDriver(GlobalApplicationContext globalApplicationContext) throws WebEngineException {
        try {
            GlobalConfigProperties globalConfigProperties = GlobalConfigPropertiesHelper.getGlobalConfigProperties(globalApplicationContext.getSettings());
            Optional<WebDriver> optional = BrowserFactory.getDriver(globalConfigProperties);
            if(optional.isPresent()){
                return optional.get();
            }
        } catch (Exception e) {
            throw new WebEngineException("Error during get driver",e);
        }
        return null;
    }

    @Override
    public void cleanUp(ITestCaseContext testCaseContext) {
        try {
            ((TestCaseWebContext)testCaseContext).getWebDriver().quit();
            loggerService.info("Browser close properly");
        }catch (NoSuchSessionException e){
            loggerService.warn("Warning during quit browser",e);
        }
    }
}
