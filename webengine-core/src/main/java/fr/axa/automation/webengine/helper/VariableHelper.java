package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.Variable;

public class VariableHelper {

    public static Variable getVariable(String name, String value){
        Variable variable = new Variable();
        variable.setName(name);
        variable.setValue(value);
        return variable;
    }
}
