package fr.axa.automation.webengine.context;

import fr.axa.automation.webengine.generated.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharedContext {
    public static final List<Variable> CONTEXT_VALUE_LIST = Collections.synchronizedList(new ArrayList());
}
