package fr.axa.automation.webengine.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedInformation {
    public static final Map<String,List<String>> INFORMATION = new ConcurrentHashMap<>();

    public static void addInformation(String key,String information){
        SharedInformation.INFORMATION.computeIfAbsent(key, k -> new ArrayList<>()).add(information);
    }
}
