package fr.axa.automation.webengine.global;

import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.TestCaseNodeNoCode;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@SuperBuilder
public class TestCaseNoCodeContext extends AbstractTestCaseContext {
    TestCaseNodeNoCode testCaseToRun;
    String dataTestColumnName;
    TestSuiteDataNoCode testSuiteData;
    Map<String,Object> driverByCommandData;

    public Object getWebDriver(CommandDataNoCode commandDataNoCode){
        return driverByCommandData.get(commandDataNoCode.getUid());
    }
}
