package fr.axa.automation.feature;

import org.junit.platform.suite.api.*;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "fr.axa.automation.feature.step")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/report-gherkin/report.html, fr.axa.automation.webengine.listener.WebengineReportListener, json:target/cucumber-report/cucumber.json")
public class CucumberRunnerTest {

}
