package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.GlobalApplicationContext;

public interface ITestExecutor {

    default Object initialize(GlobalApplicationContext globalApplicationContext) throws WebEngineException{
        return null;
    }

    default void cleanUp(Object object) throws WebEngineException{

    }
}
