package fr.axa.automation.webengine.logger;

import fr.axa.automation.webengine.context.ExecutionDetail;
import fr.axa.automation.webengine.util.ListUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Logger {

    public static final Map<String, List<String>> INFO = new ConcurrentHashMap<>();

    public static final Map<String, List<String>> WARN = new ConcurrentHashMap<>();

    public static final Map<String, List<String>> ERROR = new ConcurrentHashMap<>();

    public static final Map<String, List<String>> FATAL = new ConcurrentHashMap<>();


    public static void info(String information){
        log(INFO, information);
    }

    public static void warn(String warning){
        log(WARN, warning);
    }

    public static void error(String error){
        log(ERROR, error);
    }

    public static void fatal(String fatal){
        log(FATAL, fatal);
    }

    private static void log(Map<String, List<String>> map,String information) {
        if(CollectionUtils.isNotEmpty(ExecutionDetail.STEP_IN_PROGRESS)){
            Optional<String> optionalKey = ListUtil.getLastElement(ExecutionDetail.STEP_IN_PROGRESS);
            if(optionalKey.isPresent()){
                String key = optionalKey.get();
                map.computeIfAbsent(key, k -> new ArrayList<>()).add(information);
            }
        }
    }
}
