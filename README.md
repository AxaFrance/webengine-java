# Introduction Webengine framework


A Test Automation Framework (TAF) is a set of guidelines, libraries for creating and designing test cases. It is conceptual part of the automated testing, provides common functionalities and best practices that helps Test Automation Engineers to create reliable, maintainable, and well-structured Test Automation Solutions (TAS).

AXA WebEngine Framework makes it easier to build highly effective test automation solutions for  **Web**,  **Mobile Web**  and  **Mobile App**  testing.

Built by Test Guild of AXA France, available in  **.NET**  and  **JAVA**  and used in dozens of test automation projects within AXA France. We decided to open source this framework to share our knowledge on Test Automation to the community and hopes we can improve the framework (and the quality of IT Systems, or course  😉) together.

## Why WebEngine Framework?

WebEngine Framework resolves some common problems every test automation project can face, and it simplifies the writing of test scripts.

-   It supports all Selenium based technologies on Desktop Browsers, Mobile Browsers on different Mobile Devices.
-   Using  **Browser Factory**, there is no need to download various selenium WebDriver when browser and its version changes.
-   Description oriented  **Web Element**  identification and synchronization with browser out-of-box.
-   Organizing Web Element in  **Page Model**  makes test scripts easy to read and to maintain.
-   Fully object-oriented and compatible with  **Keyword-Driven**  and  **Data-driven**  approach.
-   Compatible with other another Unit test frameworks such as NUnit, Cucumber
-   Easy configuration and Set-up for Execution: Run test directly from Excel, as well as on DevOps platforms
-   Graphical test report.
-   **Open Source**, free usage and let’s improve it together!

## Prerequisites 

Java 8 or higher 

Maven

## How to create a maven project with linear approach
You can found this code in the module **webengine-testpage-linear**

Below an example of file`pom.xml`` with the code

pom.xml :
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>fr.axa.automation.webengine</groupId>
        <artifactId>webengine-parent</artifactId>
        <version>3.0.6</version>
    </parent>

    <artifactId>webengine-test-linear</artifactId>
    <version>3.0.6</version>
    <packaging>jar</packaging>
    <name>webengine-test-linear</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>

        <dependency>
            <groupId>fr.axa.automation.webengine</groupId>
            <artifactId>webengine-web</artifactId>
            <version>${project.parent.version}</version>
        </dependency>

    </dependencies>

    <dependencyManagement>
        <dependencies>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <configuration>
                    <useSystemClassLoader>false</useSystemClassLoader>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
            </plugin>

        </plugins>
        <pluginManagement>
            <plugins />
        </pluginManagement>
    </build>
</project>
```
linearExample.java :
```
@Test
public void linearApproachTest() throws WebEngineException {
    String baseUrl = "https://axafrance.github.io/webengine-dotnet/demo/Test.html";
    if(optionalWebdriver.isPresent()){
        WebDriver driver = optionalWebdriver.get();
        driver.get(baseUrl);
        driver.findElement(By.id("btnButtonOk")).click();
        driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
    }
}
```

## How to create a maven project with Gherkin approach
You can found this code in the module **webengine-boot-gherkin** 

Below an example of file`pom.xml`` with code

pom.xml :
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>

  <groupId>fr.axa.automation.webengine</groupId>
  <artifactId>sample-gherkin-webengine-java</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>
  <name>sample-gherkin-webengine-java</name>

  <properties>
    <maven-compiler-plugin.version>3.10.1</maven-compiler-plugin.version>
    <maven-surefire-plugin.version>3.0.0-M7</maven-surefire-plugin.version>
    <webengine-boot-gherkin.version>3.0.6</webengine-boot-gherkin.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>fr.axa.automation.webengine</groupId>
      <artifactId>webengine-boot-gherkin</artifactId>
      <version>${webengine-boot-gherkin.version}</version>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>${maven-compiler-plugin.version}</version>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>${maven-surefire-plugin.version}</version>
      </plugin>

    </plugins>
  </build>
</project>

```
sample.feature :
```
Feature: Test du parcours

  Background:
    Given I visit the test page "https://axafrance.github.io/webengine-dotnet/demo/Test.html" for running journey
    When I click on the link Start step 1

  @flow
  Scenario: Step1
    And I choose the language with text "Français"
    And I want to buy a coffee
    And I click on the first next button
```

WebEngineHomeTestPage.java :
```
package fr.axa.automation.feature.model;

import fr.axa.automation.webengine.core.AbstractPageModel;
import fr.axa.automation.webengine.core.WebElementDescription;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.WebDriver;


@FieldDefaults(level = AccessLevel.PUBLIC)
public class WebEngineHomeTestPage extends AbstractPageModel {

    WebElementDescription okButton = WebElementDescription.builder().tagName("input").id("btnButtonOk").build();

    WebElementDescription confirmButton = WebElementDescription.builder().tagName("input").id("btnButtonConfirm").build();

    WebElementDescription inputButton = WebElementDescription.builder().tagName("input").id("btnButtonInput").build();

    WebElementDescription textInput = WebElementDescription.builder().tagName("input").id("inputValue").build();

    WebElementDescription passwordInput = WebElementDescription.builder().tagName("password").id("password").build();

    WebElementDescription startStep1Link = WebElementDescription.builder().tagName("a").linkText("Step1.html").build();

    public WebEngineHomeTestPage(WebDriver webDriver) throws Exception {
        populateDriver(webDriver);
    }
}

```

SampleFormStep.java :
```
package fr.axa.automation.feature.step;


import fr.axa.automation.feature.model.WebEngineFirstStepPage;
import fr.axa.automation.feature.model.WebEngineFourthStepPage;
import fr.axa.automation.feature.model.WebEngineHomeTestPage;
import fr.axa.automation.feature.model.WebEngineSecondStepPage;
import fr.axa.automation.feature.model.WebEngineThirdStepPage;
import fr.axa.automation.webengine.step.AbstractStep;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;


@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class SampleFlowStep extends AbstractStep {

    WebDriver driver;
    
    WebEngineHomeTestPage webEngineHomeTestPage;

    WebEngineFirstStepPage webEngineFirstStepPage;
    

    public SampleFlowStep() throws Exception {
        driver = Hook.webDriver;
        webEngineHomeTestPage = new WebEngineHomeTestPage(driver);
        webEngineFirstStepPage = new  WebEngineFirstStepPage(driver);       
    }

    @Given("^I visit the test page \"([^\"]*)\" for running journey$")
    public void visitTheTestPage(String url) throws InterruptedException {
        addInformation("Open WebEngine test page");
        driver.get(url);
        getWebEngineHomeTestPage().sync(3); 
        getWebEngineHomeTestPage().maximize(); 
    }

    @And("^I click on the link Start step 1$")
    public void chooseTheLanguage() throws Exception {
        addInformation("Click on the link start step 1");
        getWebEngineHomeTestPage().startStep1Link.click();
    }

    @And("^I choose the language with text \"([^\"]*)\"$")
    public void chooseTheLanguage(String language) throws Exception {
        addInformation("Choose the language");
        getWebEngineFirstStepPage().language.selectByText(language);
    }

    @And("^I want to buy a coffee$")
    public void seePopUpAndEnterText() throws Exception {
        getWebEngineFirstStepPage().coffeeRadio.click();
    }

    @And("^I click on the first next button$")
    public void clickFirstButtonOKInThePopup() throws Exception {
        getWebEngineFirstStepPage().nextStep.click();
    }    
}

```

CucumberRunnerTest.java :
```
import org.junit.platform.suite.api.*;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameters({
                            @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "fr.axa.automation.feature.step"),
                            @ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/report-gherkin/report.html, fr.axa.automation.webengine.listener.WebengineReportListener, json:target/cucumber-report/cucumber.json")
                        })
public class CucumberRunnerTest {
}
```

## How to create a maven project with with keyword-driven approach

You can found this code in the module **webengine-testpage-keyword-driven**

For your information, we use spring-boot for this approach

For executing the project, you can run this command : 

**java -jar project-name-1.0.0-exec.jar -Dspring.profiles.active=properties-chrome "-data:C:\work\project\src\main\resources\data.xml" "-env:C:\work\project\src\main\resources\env.xml"**

With filter test case
**java -jar project-name-1.0.0-exec.jar -Dspring.profiles.active=properties-chrome "-data:C:\work\project\src\main\resources\data.xml" "-env:C:\work\project\src\main\resources\env.xml" "-testcase:TEST_CASE_1"**


If you want run the project from addin excel, you can run this command 

**java -jar WebRunner.jar "-a:C:\work\project\project-name-1.0.0-exec.jar" "-data:-data:C:\work\project\src\main\resources\data.xml" "-env:-data:C:\work\project\src\main\resources\env.xml" "-platform:Android" "-browser:Chrome"**


Below an example :

env.xml
```
<?xml version="1.0" encoding="utf-8"?>
<EnvironmentVariables xmlns="http://www.axa.fr/WebEngine/2022">
  <Variable>
    <Name>NOM</Name>
    <Value>VALEUR</Value>
  </Variable>
  <Variable>
    <Name>URL</Name>
    <Value>https://axafrance.github.io/webengine-dotnet/demo/Test.html</Value>
  </Variable>
  <Variable>
    <Name>URL_PROD</Name>
    <Value>https://axafrance.github.io/webengine-dotnet/demo/Test.html</Value>
  </Variable>
</EnvironmentVariables>
```


data.xml
```
<?xml version="1.0" encoding="utf-8"?>
<TestSuiteData xmlns="http://www.axa.fr/WebEngine/2022">
  <TestData>
    <TestName>TEST_CASE_1</TestName>
    <Data>
      <Variable>
        <Name>TEST_CASE_DESCRIPTION</Name>
        <Value>First test case</Value>
      </Variable>
      <Variable>
        <Name>LANGUAGE</Name>
        <Value>fr</Value>
      </Variable>
      <Variable>
        <Name>COMMENT</Name>
        <Value>We test the flow</Value>
      </Variable>
      <Variable>
        <Name>DATE</Name>
        <Value>11/03/2023</Value>
      </Variable>      
    </Data>
  </TestData>
  <TestData>
    <TestName>TEST_CASE_2</TestName>
    <Data>
      <Variable>
        <Name>TEST_CASE_DESCRIPTION</Name>
        <Value>Second test case</Value>
      </Variable>
      <Variable>
        <Name>COMMENTAIRE</Name>
        <Value />
      </Variable>
    </Data>
  </TestData>
</TestSuiteData>
```

application.properties

```
application:
    name: axa-fr-automation
    platformName: ANDROID
    browserName: CHROME  
appiumSettings:
    gridConnection: https://hub-cloud.browserstack.com/wd/hub
    userName: $(userName)
    password: $(password)
    localTestingConfiguration:
        activate: true
        arguments:
            force: true
            forcelocal: true
            binarypath: C:\\BrowserStack\\BrowserStackLocal.exe
            localIdentifier: XXXXYYYY
    capabilities:
        desiredCapabilitiesMap:
            geoLocation: FR => en fonction des applications
            deviceName: Samsung Galaxy S20 Ultra
            osVersion: 10.0
            projectName: e-decla-automation
            buildName: e-decla-automation-mobile
            sessionName: Samsung
            local: true
            networkLogs: true
            localIdentifier: XXXXYYYY
```


Application.java

```
import fr.axa.automation.webengine.boot.BootProject;
import fr.axa.automation.webengine.logger.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application implements CommandLineRunner {

    final LoggerService loggerService;

    final BootProject bootProject;

    @Autowired
    public Application(LoggerService loggerService, BootProject bootProject) {
        this.loggerService = loggerService;
        this.bootProject = bootProject;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(WebApplicationType.NONE).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        loggerService.info("Temporary directory application: "+System.getProperty("java.io.tmpdir"));
        bootProject.runFromProject(args);
    }
}
```

WebengineTestSuite.java (You have to extend AbstractTestSuite)
```
package fr.axa.automation.testsuite;

import com.google.common.collect.ImmutableMap;
import fr.axa.automation.testcase.FlowTestCase;
import fr.axa.automation.testcase.SimpleTestCase;
import fr.axa.automation.webengine.core.AbstractTestSuite;
import fr.axa.automation.webengine.core.ITestCase;

import java.util.Map;

public class WebengineTestSuite extends AbstractTestSuite {
    public static final String TEST_CASE_1 = "TEST_CASE_1";    

    public Map<String, ? extends ITestCase> getTestCaseList() {
        return ImmutableMap.of( TEST_CASE_1, new FlowTestCase());
    }
}

```

FlowTestCase.java (you have to implements ITestCase)
```
import fr.axa.automation.teststep.*;
import fr.axa.automation.webengine.core.ITestCase;
import fr.axa.automation.webengine.core.ITestStep;

import java.util.Arrays;
import java.util.List;

public class FlowTestCase implements ITestCase {

    @Override
    public List<? extends ITestStep> getTestStepList() {
        return Arrays.asList(new HomeTestStep(), new FirstTestStep());
    }
}
```


HomeTestStep.java (you have to implement ITestStep)
```
package fr.axa.automation.teststep;

import fr.axa.automation.action.HomeAction;
import fr.axa.automation.webengine.core.IAction;
import fr.axa.automation.webengine.core.ITestStep;

public class HomeTestStep implements ITestStep {
    @Override
    public Class<? extends IAction> getAction() {
        return HomeAction.class;
    }
}
```

FirstTestStep.java (you have to implement ITestStep)
```
package fr.axa.automation.teststep;

import fr.axa.automation.action.FirstStepAction;
import fr.axa.automation.webengine.core.IAction;
import fr.axa.automation.webengine.core.ITestStep;

public class FirstTestStep implements ITestStep {
    @Override
    public Class<? extends IAction> getAction() {
        return FirstStepAction.class;
    }
}
```

HomeAction.java (You have to extends AbstractActionWebBase)

```
import fr.axa.automation.appmodels.WebEngineHomeTestPage;
import fr.axa.automation.parameter.IParameter;
import fr.axa.automation.webengine.core.AbstractActionWebBase;
import fr.axa.automation.webengine.helper.VariableHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HomeAction extends AbstractActionWebBase {

    WebEngineHomeTestPage webEngineHomeTestPage;

    public HomeAction() {
    }

    @Override
    public void doAction() throws Exception {
        webEngineHomeTestPage = new WebEngineHomeTestPage(getWebDriver());
        String url = getEnvironnementValueWithException(IParameter.URL);
        getWebDriver().get(url);
        webEngineHomeTestPage.startStep1Link.click();
        screenShot();
        addInformation("Home page");
        setContextValue(VariableHelper.getVariable("HOME_PAGE","SUCCESS"));
    }

    @Override
    public boolean doCheckpoint() throws Exception {
        return true;
    }
}
```

FirstStepAction.java (You have to extends AbstractActionWebBase)
```
import fr.axa.automation.appmodels.WebEngineFirstStepPage;
import fr.axa.automation.parameter.IParameter;
import fr.axa.automation.webengine.core.AbstractActionWebBase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FirstStepAction extends AbstractActionWebBase {
WebEngineFirstStepPage webEngineFirstStepPage;
public FirstStepAction() {
}

    @Override
    public void doAction() throws Exception {
        String language = getParameterWithException(IParameter.LANGUAGE);
        webEngineFirstStepPage = new WebEngineFirstStepPage(getWebDriver());
        webEngineFirstStepPage.language.selectByValue(language);
        webEngineFirstStepPage.coffeeRadio.click();
        webEngineFirstStepPage.nextStep.click();
        screenShot();
        addInformation("First step succeed");
    }

    @Override
    public boolean doCheckpoint() throws Exception {
        return true;
    }
}
```