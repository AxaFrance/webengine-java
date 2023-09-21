package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.logger.ILoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActionWebExecutor extends AbstractActionExecutor {

    @Autowired
    public ActionWebExecutor(ILoggerService loggerService) {
        super(loggerService);
    }
}
