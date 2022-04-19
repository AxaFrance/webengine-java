package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.util.BrowserFactory;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
public class TestCaseExecutorWeb extends AbstractTestCaseExecutor {
    @Override
    public Object initialize(GlobalApplicationContext globalApplicationContext) throws WebEngineException {
        try {
            return BrowserFactory.getDriver(globalApplicationContext.getSettings());
        } catch (Exception e) {
            throw new WebEngineException("Error during get driver",e);
        }
    }

    @Override
    public void cleanUp(Object object) {
        ((WebDriver)object).close();
        ((WebDriver)object).quit();
    }
}
