package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.object.CommandResult;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class OpenInPrivateModeCommand extends OpenCommand{

    @Override
    protected WebDriver instantiateWebDriver(AbstractGlobalApplicationContext globalApplicationContext, List<CommandResult> commandResultList) throws WebEngineException {
        return initializeIncognitoWebDriver(globalApplicationContext);
    }
}
