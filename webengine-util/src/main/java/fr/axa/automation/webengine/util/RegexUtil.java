package fr.axa.automation.webengine.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class RegexUtil {

    public static List<String> match(String patternExpression, String matchExpression){
        List<String> resultList = new ArrayList<>();
        Pattern pattern = Pattern.compile(patternExpression);
        Matcher matcher = pattern.matcher(matchExpression);

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

        return resultList.stream().distinct().collect(Collectors.toList());
    }

    public static Optional<String> findFirst(String patternExpression, String matchExpression){
        List<String> matchList = match(patternExpression,matchExpression);
        if(CollectionUtils.isNotEmpty(matchList)){
            return matchList.stream().findFirst();
        }
        return Optional.empty();
    }

    public static Integer getNumber(String regex, String value){
        Integer number = 0;
        List<String> regexValueList = RegexUtil.match(regex, value);
        if (CollectionUtils.isNotEmpty(regexValueList)) {
            return Integer.parseInt(regexValueList.get(0));
        }
        return number;
    }

}
