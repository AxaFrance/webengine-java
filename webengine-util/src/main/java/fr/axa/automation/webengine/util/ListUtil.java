package fr.axa.automation.webengine.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class ListUtil {

    private ListUtil() {
    }

    public static Optional<String> findFirst(List<String> list, String search)  {
        Optional<String> findFirstOptional = list.stream().filter(s->s.contains(search)).findFirst();
        return findFirstOptional;
    }

    public static List<Class> getClasses(Object[] parameters) {
        return Arrays.asList(parameters).stream().map(o -> o.getClass()).collect(Collectors.toList());
    }

    public static <T> Set<T> findDuplicateElements(List<T> list) {
        Set<T> items = new HashSet<>();
        return list.stream()
                .filter(n -> !items.add(n)) // Set.add() returns false if the element was already in the set.
                .collect(Collectors.toSet());

    }
}
