package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.util.StringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NameNormalizerHelper {

    public static final Map<String, String> SCENARIO_NAME = new ConcurrentHashMap<>();

    public static String getNormalizeName(String featureName, String testCaseName) {
        String normalizeName = StringUtil.getNormalizeString(new String[]{featureName,testCaseName},StringUtil.DOUBLE_TWO_POINTS);
        SCENARIO_NAME.put(normalizeName, testCaseName);
        return normalizeName;
    }

    public static String getNormalizeName(String featureName, String testCaseName, String testStepName) {
        return StringUtil.getNormalizeString(new String[]{featureName,testCaseName,testStepName}, StringUtil.DOUBLE_TWO_POINTS);
     }
}
