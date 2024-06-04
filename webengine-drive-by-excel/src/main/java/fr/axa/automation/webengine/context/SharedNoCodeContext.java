package fr.axa.automation.webengine.context;

import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedNoCodeContext {
    public static final Map<String,String> ADDITIONAL_DATA = new ConcurrentHashMap<>();

    public static void addAdditionalData(String key, String information){
        String value = information;
        if(StringUtils.isEmpty(value)){
            value = StringUtil.EMPTY;
        }
        SharedNoCodeContext.ADDITIONAL_DATA.put(key,value);
    }
}
