package fr.axa.automation.webengine.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class ListUtil {

    private ListUtil() {
    }

    public static Optional<String> findFirst(List<String> list, String search)  {
        return list.stream().filter(s->s.contains(search)).findFirst();
    }

    public static <T> Optional<T> getLastElement(Collection<T> list)  {
        return list.stream().reduce((first, second) -> second);
    }

    public static List<Class> getClasses(Object[] parameters) {
        return Arrays.asList(parameters).stream().map(o -> o.getClass()).collect(Collectors.toList());
    }

    public static <T> Set<T> findDuplicateElements(Collection<T> list) {
        Set<T> items = new HashSet<>();
        return list.stream()
                .filter(n -> !items.add(n)) // Set.add() returns false if the element was already in the set.
                .collect(Collectors.toSet());

    }

    public static <T> Map<T,Integer> findNumberOfElements(Collection<T> list) {
        Map<T,Integer> resultMap = new HashMap<>();
        for (T element : list) {
            if (resultMap.containsKey(element)) {
                resultMap.put(element, resultMap.get(element) + 1);
            } else {
                resultMap.put(element, 1);
            }
        }
        return resultMap;
    }
}
