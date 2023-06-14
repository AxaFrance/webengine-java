package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.object.CommandResult;
import org.junit.jupiter.api.Test;

import java.util.List;

class EvaluateValueTest {

    @Test
    void todayTest() {
        String value = "<<<TODAY>>>";
        List<CommandResult> commandResultList = null;
        String result = EvaluateValueHelper.evaluateValue(null,value,commandResultList);
        System.out.println(result);
    }

    @Test
    void todayPlusDayTest() {
        String value = "<<<TODAY+10>>>";
        List<CommandResult> commandResultList = null;
        String result = EvaluateValueHelper.evaluateValue(null,value,commandResultList);
        System.out.println(result);
    }

    @Test
    void todayHourTest() {
        String value = "<<<today_hour>>>";
        List<CommandResult> commandResultList = null;
        String result = EvaluateValueHelper.evaluateValue(null,value,commandResultList);
        System.out.println(result);
    }
}