package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.logger.ILoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActionExecutorWeb extends AbstractActionExecutor {

    @Autowired
    public ActionExecutorWeb(ILoggerService loggerService) {
        super(loggerService);
    }
}
