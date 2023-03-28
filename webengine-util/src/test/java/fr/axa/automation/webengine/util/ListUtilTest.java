package fr.axa.automation.webengine.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class ListUtilTest {

    @Test
    void testFindFirst() {
        List<String> list = Arrays.asList("One","Two","Three");
        Optional<String> firstOptional = ListUtil.findFirst(list,"One");
        if(firstOptional.isPresent()){
            Assertions.assertEquals("One",firstOptional.get());
        }
    }

    @Test
    void testGetLastElement() {
        List<String> list = Arrays.asList("One","Two","Three");
        Optional<String> firstOptional = ListUtil.getLastElement(list);
        if(firstOptional.isPresent()){
            Assertions.assertEquals("Three",firstOptional.get());
        }
    }

    @Test
    void testFindDuplicateElements() {
        // 3, 4, 9
        List<Integer> list = Arrays.asList(5, 3, 4, 1, 3, 7, 2, 9, 9, 4);
        Set<Integer> result = ListUtil.findDuplicateElements(list);
        Assertions.assertEquals(Arrays.asList(3,4,9),result.stream().collect(Collectors.toList()));
    }
}