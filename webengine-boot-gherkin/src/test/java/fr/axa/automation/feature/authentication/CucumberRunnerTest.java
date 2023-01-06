package fr.axa.automation.feature.authentication;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(   features = "src/test/resources/features",
                    glue = "fr.axa.automation.feature",
                    plugin = {"pretty" , "html:target/report-gherkin/report.html","fr.axa.automation.webengine.listener.WebengineReportListener", "json:target/cucumber-report/cucumber.json"} )
public class CucumberRunnerTest {

}
