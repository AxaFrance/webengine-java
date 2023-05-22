package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.Variable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;


public final class EnvironmentVariablesHelper {

    private EnvironmentVariablesHelper() {
    }

    public static Optional<Variable> getEnvironnementValue(String name, List<Variable> environnementVariableList){
        Optional<Variable> variable = Optional.empty();
        if(StringUtils.isNotEmpty(name) && CollectionUtils.isNotEmpty(environnementVariableList)){
            variable = environnementVariableList.stream().filter(x->name.equalsIgnoreCase(x.getName())).findFirst();
            if(variable.isPresent() && variable.get().getValue().startsWith("$")){
                variable = getEnvironnementValue(variable.get().getValue().substring(1),environnementVariableList);
            }
        }
        return variable;
    }
}
