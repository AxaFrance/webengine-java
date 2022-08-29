package fr.axa.automation.webengine.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    public static final String UNDERSCORE = "_";

    public static String removeSpecialCharacters(String text){
       return StringUtils.stripAccents(text).replaceAll(StringUtils.SPACE, UNDERSCORE).toUpperCase();
    }
}
