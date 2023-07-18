package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import org.openqa.selenium.WebDriver;

public class OpenInPrivateModeCommand extends OpenCommand{

    @Override
    protected WebDriver instantiateWebDrive(AbstractGlobalApplicationContext globalApplicationContext) throws WebEngineException {
        return initializeIncognitoWebDriver(globalApplicationContext);
    }
}
