package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.generated.Variable;
import fr.axa.automation.webengine.helper.VariableHelper;

import java.util.Arrays;
import java.util.List;

public class VariableHelperForTest {

    public static final String KEY_CHROME = "chrome";
    public static final String VALUE_CHROME = "google";
    public static final String KEY_EDGE_CHROMIUM = "edgeChromium";
    public static final String VALUE_EDGE_CHROMIUM = "microsoft";

    public static final String VARIABLE_REFERENCE_PREFICE = "$";

    public static List<Variable> getVariableList(){
        Variable variable1 = VariableHelper.getVariable(KEY_CHROME,VALUE_CHROME);
        Variable variable2 = VariableHelper.getVariable(KEY_EDGE_CHROMIUM,VALUE_EDGE_CHROMIUM);
        return Arrays.asList(variable1,variable2);
    }

    public static List<Variable> getVariableWithReferenceList(){
        Variable variable1 = VariableHelper.getVariable(KEY_CHROME, VARIABLE_REFERENCE_PREFICE + KEY_EDGE_CHROMIUM);
        Variable variable2 = VariableHelper.getVariable(KEY_EDGE_CHROMIUM,VALUE_EDGE_CHROMIUM);
        return Arrays.asList(variable1,variable2);
    }
}
