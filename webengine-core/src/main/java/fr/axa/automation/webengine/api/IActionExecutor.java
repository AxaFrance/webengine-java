package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.report.object.ActionReportDetail;

public interface IActionExecutor {
    ActionReportDetail run(IAction actionDetail) throws WebEngineException;
}
