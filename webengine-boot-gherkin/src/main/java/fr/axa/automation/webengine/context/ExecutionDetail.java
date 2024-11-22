package fr.axa.automation.webengine.context;

import fr.axa.automation.webengine.util.ListUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class ExecutionDetail {

    public static final Set<String> STEP_IN_PROGRESS = Collections.synchronizedSet(new LinkedHashSet<>()) ;
}
