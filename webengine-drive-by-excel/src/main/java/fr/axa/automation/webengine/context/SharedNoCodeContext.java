package fr.axa.automation.webengine.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedNoCodeContext {
    public static final Map<String,String> ADDITIONAL_DATA = new ConcurrentHashMap<>();

    public static void addAdditionalData(String key, String information){
        SharedNoCodeContext.ADDITIONAL_DATA.put(key,information);
    }
}
