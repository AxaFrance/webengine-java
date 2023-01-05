package fr.axa.automation.webengine.context;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ExecutionDetail {

    public static final Set<String> STEP_IN_PROGRESS = Collections.synchronizedSet(new LinkedHashSet<>()) ;

}
