package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.report.ActionReportDetail;

public interface IActionExecutor {
    ActionReportDetail run(GlobalApplicationContext globalApplicationContext, IAction actionDetail) throws WebEngineException;
}
