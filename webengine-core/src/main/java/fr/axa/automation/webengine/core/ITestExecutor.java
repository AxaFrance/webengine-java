package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;

public interface ITestExecutor {

    default Object initialize(AbstractGlobalApplicationContext globalApplicationContext) throws WebEngineException{
        return null;
    }

    default void cleanUp(Object object) throws WebEngineException{
    }
}
