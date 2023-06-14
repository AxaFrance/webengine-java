package fr.axa.automation.webengine.core;


import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.helper.GlobalConfigPropertiesHelper;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.util.BrowserFactory;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

public abstract class AbstractTestCaseWebExecutor extends AbstractTestCaseExecutor {

    public AbstractTestCaseWebExecutor(ITestStepExecutor testStepExecutor, GlobalConfiguration globalConfiguration, ILoggerService loggerService ) {
        super(testStepExecutor, globalConfiguration, loggerService);
    }

    @Override
    public Object initializeWebDriver(AbstractGlobalApplicationContext globalApplicationContext) throws WebEngineException {
        try {
            GlobalConfiguration globalConfiguration = GlobalConfigPropertiesHelper.getGlobalConfigProperties(globalApplicationContext.getSettings());
            Optional<WebDriver> optional = BrowserFactory.getDriver(globalConfiguration);
            if(optional.isPresent()){
                return optional.get();
            }
        } catch (Exception e) {
            throw new WebEngineException("Error during get driver",e);
        }
        return null;
    }

    @Override
    public void cleanUp(Object testCaseContext) {
        try {
            ((WebDriver)((AbstractTestCaseContext)testCaseContext).getWebDriver()).quit();
            loggerService.info("Browser close properly");
        }catch (NoSuchSessionException e){
            loggerService.warn("Warning during quit browser",e);
        }
    }
}
