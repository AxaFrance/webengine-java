package fr.axa.automation.webengine.global;

import fr.axa.automation.webengine.object.TestCaseNodeNoCode;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.openqa.selenium.WebDriver;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@SuperBuilder
public class TestCaseNoCodeContext extends AbstractTestCaseContext {
    TestSuiteDataNoCode testSuiteData;
    TestCaseNodeNoCode testCaseToRun;
    String dataTestColumnName;
    List<WebDriver> webDriverList;
}
