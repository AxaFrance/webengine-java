package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.Variable;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class EnvironmentVariablesHelper {
    List<Variable> variableList;

    public String getValue(String name) throws WebEngineException {
        String value = null;
        if(StringUtils.isEmpty(name)){
            throw new WebEngineException("Parameter name is null");
        }
        if(CollectionUtils.isEmpty(variableList)){
            throw new WebEngineException("Environnement variables not initialized");
        }

        if(variableList.stream().noneMatch(elt -> name.equals(elt.getName()))){
            StringBuilder stringBuffer = new StringBuilder("Environnement variable").append(name).append("not found");
            log.debug(stringBuffer.toString());
        }else{
            value = getConcreteValue(name).getValue();
            while (value != null && value.startsWith("$")){
                value = getConcreteValue(value).getValue();
            }
        }
        return value;
    }

    private Variable getConcreteValue(String name) {
        return variableList
                .stream()
                .filter(elt -> name.equals(elt.getName()))
                .findFirst()
                .orElse(null);
    }

    public static Optional<Variable> getEnvironnementValue(String name, List<Variable> environnementVariableList){
        Optional<Variable> variable = null;
        if(StringUtils.isNotEmpty(name) && CollectionUtils.isNotEmpty(environnementVariableList)){
            variable = environnementVariableList.stream().filter(x->name.equalsIgnoreCase(x.getName())).findFirst();
        }
        return variable;
    }
}
