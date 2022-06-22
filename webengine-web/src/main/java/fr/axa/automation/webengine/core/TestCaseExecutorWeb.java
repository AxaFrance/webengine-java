package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.util.BrowserFactory;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TestCaseExecutorWeb extends AbstractTestCaseExecutor {
    @Override
    public Object initialize(GlobalApplicationContext globalApplicationContext) throws WebEngineException {
        try {
            Optional<WebDriver> optional = BrowserFactory.getDriver(globalApplicationContext.getSettings());
            if(optional.isPresent()){
                return optional.get();
            }
        } catch (Exception e) {
            throw new WebEngineException("Error during get driver",e);
        }
        return null;
    }

    @Override
    public void cleanUp(Object object) {
//        ((WebDriver)object).close();
        try {
            ((WebDriver)object).quit();
        }catch (NoSuchSessionException e){
            loggerService.warn("Warning during quit browser",e);
        }
    }
}
