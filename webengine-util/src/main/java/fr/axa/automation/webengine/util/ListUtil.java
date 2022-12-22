package fr.axa.automation.webengine.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ListUtil {
    public static Optional<String> findFirst(List<String> list, String search)  {
        Optional<String> findFirstOptional = list.stream().filter(s->s.contains(search)).findFirst();
        return findFirstOptional.isPresent() ? findFirstOptional : Optional.empty();
    }

    public static List<Class> getClasses(Object[] parameters) {
        return Arrays.asList(parameters).stream().map(o -> o.getClass()).collect(Collectors.toList());
    }
}
