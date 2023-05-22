package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;

public interface ITestExecutor {
    Object initialize(GlobalApplicationContext globalApplicationContext) throws WebEngineException;
    void cleanUp(Object object) throws WebEngineException;
}
