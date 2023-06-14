package fr.axa.automation.webengine.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CommandDataHelperTest {

    @Test
    void mergeLists1() {
        List<String> dataTestColumList = Arrays.asList("data-test-1","data-test-2","data-test-3");
        List<String> dataTestRefList = new ArrayList<>();
        List<String> result = CommandDataHelper.mergeLists(dataTestRefList,dataTestColumList);
        Assertions.assertTrue(result.size()==3);
    }


    @Test
    void mergeLists2() {
        List<String> dataTestColumList = Arrays.asList("data-test-1","data-test-2","data-test-3");
        List<String> dataTestRefList = Arrays.asList("data-test-1","data-test-2");
        List<String> result = CommandDataHelper.mergeLists(dataTestRefList,dataTestColumList);
        Assertions.assertTrue(result.size()==2);
    }

    @Test
    void mergeLists3() {
        List<String> dataTestColumList = Arrays.asList("data-test-1","data-test-2","data-test-3");
        List<String> dataTestRefList = Arrays.asList("!data-test-1");
        List<String> result = CommandDataHelper.mergeLists(dataTestRefList,dataTestColumList);
        Assertions.assertTrue(result.size()==2);
    }

    @Test
    void mergeLists4() {
        List<String> dataTestColumList = Arrays.asList("data-test-1","data-test-2","data-test-3");
        List<String> dataTestRefList = Arrays.asList("!data-test-1","data-test-2");
        List<String> result = CommandDataHelper.mergeLists(dataTestRefList,dataTestColumList);
        Assertions.assertTrue(result.size()==1);
    }
}