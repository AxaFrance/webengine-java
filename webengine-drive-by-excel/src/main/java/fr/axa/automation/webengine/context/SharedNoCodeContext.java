package fr.axa.automation.webengine.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedNoCodeContext {
    public static final Map<String,String> CONTEXT = new ConcurrentHashMap<>();

    public static void addContext(String key,String information){
        SharedNoCodeContext.CONTEXT.putIfAbsent(key,information);
    }
}
