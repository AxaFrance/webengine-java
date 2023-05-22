package fr.axa.automation.webengine.status;

import fr.axa.automation.webengine.generated.Result;
import io.cucumber.plugin.event.Status;

import java.util.HashMap;
import java.util.Map;

public class StatusMapping {
    public final static Map<Status,Result> MAPPING;
    static {
        MAPPING = new HashMap<>();
        MAPPING.put(Status.PASSED,Result.PASSED);
        MAPPING.put(Status.FAILED,Result.FAILED);
        MAPPING.put(Status.AMBIGUOUS,Result.NONE);
        MAPPING.put(Status.SKIPPED,Result.IGNORED);
    }
}
