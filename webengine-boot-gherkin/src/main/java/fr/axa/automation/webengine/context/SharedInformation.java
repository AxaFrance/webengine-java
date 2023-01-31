package fr.axa.automation.webengine.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedInformation {
    public static final Map<String,List<String>> INFORMATION = new ConcurrentHashMap<>();

    public static void addInformation(String key,String information){
        if(SharedInformation.INFORMATION.containsKey(key)){
            SharedInformation.INFORMATION.get(key).add(information);
        }else{
            List<String> list = new ArrayList<>();
            list.add(information);
            SharedInformation.INFORMATION.put(key, list);
        }
    }
}
