package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Variable;

import java.util.List;

public interface IAction {
    ActionReport runAction() throws Exception;
    void doAction() throws Exception;
    boolean runCheckpoint() throws Exception;
    boolean doCheckpoint() throws Exception;
    void screenShot(String name) throws WebEngineException;
    default List<Variable> getRequiredParameters(){
        return null;
    }
}
