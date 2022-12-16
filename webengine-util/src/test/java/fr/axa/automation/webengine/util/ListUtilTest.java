package fr.axa.automation.webengine.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ListUtilTest {

    @Test
    void testFindFirst() {
        List<String> list = Arrays.asList("One","Two","Three");
        Optional<String> firstOptional = ListUtil.findFirst(list,"One");
        if(firstOptional.isPresent()){
            Assertions.assertEquals("One",firstOptional.get());
        }
    }
}