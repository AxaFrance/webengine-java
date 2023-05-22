package fr.axa.automation.webengine.context;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class ExecutionDetail {

    public static final Set<String> STEP_IN_PROGRESS = Collections.synchronizedSet(new LinkedHashSet<>()) ;

    public static void addInformation(String information){
        if(CollectionUtils.isNotEmpty(STEP_IN_PROGRESS)){
            Optional<String> optionalKey = STEP_IN_PROGRESS.stream().reduce((one, two) -> two); //Get last value of set
            if(optionalKey.isPresent()){
                String key = optionalKey.get();
                SharedInformation.addInformation(key,information);
            }
        }
    }

}
