package fr.axa.automation.webengine.util;

import org.apache.commons.lang3.StringUtils;

public final class StringUtil {

    public static final String UNDERSCORE = "_";
    public static final String EMPTY = "";
    public static final String DOUBLE_TWO_POINTS = "::";
    public static final String CHARACTERS_TO_REPLACE = "[ ']";

    private StringUtil() {
    }

    public static boolean equalsIgnoreCase(String value1,String value2){
        if(value1==null && value2==null){
            return true;
        } else if ((value1==null || value2==null)) {
            return false;
        } else  {
            return StringUtils.equalsIgnoreCase(value1.trim(),value2.trim());
        }
    }

    public static boolean contains(String value1,String value2){
        if(value1==null && value2==null){
            return true;
        } else if ((value1==null || value2==null)) {
            return false;
        } else  {
            return StringUtils.contains(value1.trim().toUpperCase(),value2.trim().toUpperCase());
        }
    }

    public static String removeSpecialCharacters(String text){
        return StringUtils.stripAccents(text).replaceAll(CHARACTERS_TO_REPLACE, UNDERSCORE).toUpperCase();
    }

    public static String getNormalizeString(String[] name, String delimiter){
        String normalizeName = StringUtil.EMPTY;
        if(name == null){
            return normalizeName;
        }
        if(name.length > 1){
            normalizeName = StringUtil.removeSpecialCharacters(String.join(delimiter, name));
        }else{
            normalizeName = StringUtil.removeSpecialCharacters(name[0]);
        }
        return normalizeName;
    }
}
