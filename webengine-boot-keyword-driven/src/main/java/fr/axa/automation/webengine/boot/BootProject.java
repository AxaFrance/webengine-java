package fr.axa.automation.webengine.boot;

import fr.axa.automation.webengine.api.ITestSuiteWebExecutor;
import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.core.AbstractTestSuite;
import fr.axa.automation.webengine.core.ITestSuite;
import fr.axa.automation.webengine.core.TestCaseAdditionalInformation;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.EnvironmentVariables;
import fr.axa.automation.webengine.generated.TestSuiteData;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.global.Settings;
import fr.axa.automation.webengine.helper.TestSuiteHelper;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.report.constante.ReportPathKey;
import fr.axa.automation.webengine.report.helper.global.IReportHelper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Component
@Qualifier("bootProject")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class BootProject extends AbstractBootProject{

    static final List<ArgumentOption> ARGUMENT_OPTION_FRAMEWORK = Arrays.asList(ArgumentOption.PROJECT, ArgumentOption.TEST_DATA, ArgumentOption.ENVIRONMENT_VARIABLE, ArgumentOption.PROPERTIES_FILE_LIST, ArgumentOption.BROWSER, ArgumentOption.PLATFORM, ArgumentOption.OUTPUT_DIR, ArgumentOption.MANUAL_DEBUG, ArgumentOption.JUNIT, ArgumentOption.SHOW_REPORT, ArgumentOption.TEST_CASE_TO_RUN);

    static final List<ArgumentOption> ARGUMENT_OPTION_PROJECT = Arrays.asList(ArgumentOption.TEST_DATA, ArgumentOption.ENVIRONMENT_VARIABLE, ArgumentOption.PROPERTIES_FILE_LIST, ArgumentOption.BROWSER, ArgumentOption.PLATFORM, ArgumentOption.OUTPUT_DIR, ArgumentOption.MANUAL_DEBUG, ArgumentOption.JUNIT, ArgumentOption.SHOW_REPORT, ArgumentOption.TEST_CASE_TO_RUN);

    @Autowired
    public BootProject(@Qualifier("testSuiteWebExecutor") ITestSuiteWebExecutor testSuiteExecutor, IReportHelper reportHelper, ILoggerService loggerService, GlobalConfiguration globalConfiguration) {
        super(testSuiteExecutor,reportHelper,loggerService, globalConfiguration);
    }

    @Override
    protected List<ArgumentOption> getArgumentOptionFramework() {
        return ARGUMENT_OPTION_FRAMEWORK;
    }

    @Override
    protected List<ArgumentOption> getArgumentOptionProjet() {
        return ARGUMENT_OPTION_PROJECT;
    }

    public void runTestSuite(CommandLine commandLine) throws WebEngineException, IOException {
        ITestSuite testSuite = TestSuiteHelper.getTestSuite();
        AbstractGlobalApplicationContext globalApplicationContext = getGlobalApplicationContext(commandLine, testSuite);

        testSuiteExecutor.initialize(globalApplicationContext);
        if (testSuite instanceof AbstractTestSuite) {
            ((AbstractTestSuite) testSuite).setGlobalApplicationContext(globalApplicationContext);
        }
        TestSuiteReport testSuiteReport = ((ITestSuiteWebExecutor)testSuiteExecutor).run(globalApplicationContext, testSuite);
        testSuiteExecutor.cleanUp(globalApplicationContext);
        Map<ReportPathKey,String> reportsPath = reportHelper.generateReports(testSuiteReport, testSuite.getClass().getSimpleName(), globalApplicationContext.getSettings().getOutputDir());

        if(globalApplicationContext.getSettings().isShowReport()) {
            reportHelper.openReport(reportsPath.get(ReportPathKey.HTML_REPORT_PATH_KEY) + File.separator + "index.html");
        }
        if(testSuiteReport!=null && testSuiteReport.getFailed()>0){
            String errorMsg = "Test suite failed. Total number of test case :" + testSuiteReport.getNumberOfTestcase() +". Number of failed test : "+testSuiteReport.getFailed();
            loggerService.error(errorMsg);
            throw new WebEngineException(errorMsg);
        }
    }

    public AbstractGlobalApplicationContext getGlobalApplicationContext(CommandLine commandLine, ITestSuite testSuite) throws WebEngineException, IOException {
        EnvironmentVariables environmentVariables = TestSuiteHelper.getEnvironmentVariables(commandLine);
        TestSuiteData testSuiteData = TestSuiteHelper.getTestSuiteData(commandLine);
        Settings settings = TestSuiteHelper.getSettings(commandLine, globalConfiguration);
        Map<String, TestCaseAdditionalInformation> testCaseAdditionalInformationMap = TestSuiteHelper.getTestCaseAdditionalInformation(testSuite, testSuiteData);

        return GlobalApplicationContext.builder()
                .settings(settings)
                .environmentVariables(environmentVariables)
                .testSuiteData(testSuiteData)
                .testCaseAdditionnalInformationList(testCaseAdditionalInformationMap)
                .build();
    }
}
