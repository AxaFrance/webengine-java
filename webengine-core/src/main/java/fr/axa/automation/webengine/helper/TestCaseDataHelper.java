package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.Variable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public final class TestCaseDataHelper {

    private TestCaseDataHelper() {
    }

    public static Optional<Variable> getValue(String name, List<Variable> dataList){
        if(StringUtils.isNotEmpty(name) && CollectionUtils.isNotEmpty(dataList)){
            return dataList.stream().filter(x->name.equalsIgnoreCase(x.getName())).findFirst();
        }
        return Optional.empty();
    }
}
