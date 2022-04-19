package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.Variable;

import java.util.List;
import java.util.Optional;

public class VariableHelper {


    public static void addItem(List<Variable> variableList, Variable variable) throws WebEngineException {
        if(variableList==null){
            throw new WebEngineException("Parameter variableList is null");
        }
        Variable matchVariable = findObject(variableList, variable);
        if(matchVariable==null){
            variableList.add(variable);
        }else{
            matchVariable.setValue(variable.getValue());
        }
    }

    private static Variable findObject(List<Variable> variableList, Variable variable) {
        Optional<Variable> variableOptional = variableList
                .stream()
                .filter(elt -> elt.getName().equals(variable.getName()))
                .findFirst();

        return variableOptional.orElse(null);
    }


}
