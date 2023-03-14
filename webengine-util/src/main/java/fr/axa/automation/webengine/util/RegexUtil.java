package fr.axa.automation.webengine.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtil {

    public static Set<String> match(String patternExpression, String matchExpression){
        Set<String> resultList = new HashSet<>();
        Pattern pattern = Pattern.compile(patternExpression);
        Matcher matcher = pattern.matcher(matchExpression);
        String matchValue;

        int i = 0;
        while (matcher.find()) {
            for (int j = 0; j <= matcher.groupCount(); j++) {
                if(StringUtils.isNotEmpty(matcher.group(j))){
//                    System.out.println("------------------------------------");
//                    System.out.println("Group " + i + ": " + matcher.group(j));
//                    i++;
                    resultList.add(matcher.group(j));
                }
            }
        }

        return resultList;
    }

}
