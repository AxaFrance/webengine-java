package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.object.CommandResult;
import org.junit.jupiter.api.Test;

import java.util.Map;

class EvaluateValueTest {

    @Test
    void todayTest() {
        String value = "<<<TODAY>>>";
        Map<String, CommandResult> commandResultMap = null;
        String result = EvaluateValueHelper.evaluateValue(value,commandResultMap);
        System.out.println(result);
    }

    @Test
    void todayPlusDayTest() {
        String value = "<<<TODAY+10>>>";
        Map<String, CommandResult> commandResultMap = null;
        String result = EvaluateValueHelper.evaluateValue(value,commandResultMap);
        System.out.println(result);
    }

    @Test
    void todayHourTest() {
        String value = "<<<today_hour>>>";
        Map<String, CommandResult> commandResultMap = null;
        String result = EvaluateValueHelper.evaluateValue(value,commandResultMap);
        System.out.println(result);
    }
}