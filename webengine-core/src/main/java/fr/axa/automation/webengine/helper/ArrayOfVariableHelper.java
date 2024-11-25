package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.ArrayOfVariable;
import fr.axa.automation.webengine.generated.Variable;

import java.util.List;

public class ArrayOfVariableHelper {

    private ArrayOfVariableHelper() {
    }

    public static ArrayOfVariable getArrayOfVariable(List<Variable> allLogList){
        ArrayOfVariable arrayOfVariable = new ArrayOfVariable();
        arrayOfVariable.getVariables().addAll(allLogList);
        return arrayOfVariable;
    }
}
