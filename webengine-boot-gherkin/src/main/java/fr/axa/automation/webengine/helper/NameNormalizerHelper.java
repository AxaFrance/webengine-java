package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.util.StringUtil;

public class NameNormalizerHelper {

    public static String getNormalizeName(String featureName, String testCaseName) {
        return StringUtil.getNormalizeString(new String[]{featureName,testCaseName},StringUtil.DOUBLE_TWO_POINTS);
    }

    public static String getNormalizeName(String featureName, String testCaseName, String testStepName) {
        return StringUtil.getNormalizeString(new String[]{featureName,testCaseName,testStepName}, StringUtil.DOUBLE_TWO_POINTS);
    }
}
