package fr.axa.automation.webengine.boot;

import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.argument.ArgumentParser;
import fr.axa.automation.webengine.constante.IConstant;
import fr.axa.automation.webengine.core.AbstractTestSuite;
import fr.axa.automation.webengine.core.ITestSuite;
import fr.axa.automation.webengine.core.ITestSuiteExecutor;
import fr.axa.automation.webengine.core.TestCaseAdditionalInformation;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.general.Settings;
import fr.axa.automation.webengine.generated.EnvironmentVariables;
import fr.axa.automation.webengine.generated.TestSuiteData;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.helper.TestSuiteHelper;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.report.helper.global.IReportHelper;
import fr.axa.automation.webengine.util.JarUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class BootProject {

    static final List<ArgumentOption> ARGUMENT_OPTION_FRAMEWORK = Arrays.asList(ArgumentOption.PROJECT, ArgumentOption.TEST_DATA, ArgumentOption.ENVIRONMENT_VARIABLE, ArgumentOption.PROPERTIES_FILE_LIST, ArgumentOption.BROWSER, ArgumentOption.PLATFORM, ArgumentOption.OUTPUT_DIR, ArgumentOption.MANUAL_DEBUG, ArgumentOption.JUNIT, ArgumentOption.SHOW_REPORT, ArgumentOption.TEST_CASE_TO_RUN);

    static final List<ArgumentOption> ARGUMENT_OPTION_PROJECT = Arrays.asList(ArgumentOption.TEST_DATA, ArgumentOption.ENVIRONMENT_VARIABLE, ArgumentOption.PROPERTIES_FILE_LIST, ArgumentOption.BROWSER, ArgumentOption.PLATFORM, ArgumentOption.OUTPUT_DIR, ArgumentOption.MANUAL_DEBUG, ArgumentOption.JUNIT, ArgumentOption.SHOW_REPORT, ArgumentOption.TEST_CASE_TO_RUN);

    final ILoggerService loggerService;

    final ITestSuiteExecutor testSuiteExecutor;

    final IReportHelper reportHelper;

    final GlobalConfigProperties globalConfigProperties;

    @Autowired
    public BootProject(ITestSuiteExecutor testSuiteExecutor, IReportHelper reportHelper, ILoggerService loggerService, GlobalConfigProperties globalConfigProperties) {
        this.loggerService = loggerService;
        this.testSuiteExecutor = testSuiteExecutor;
        this.reportHelper = reportHelper;
        this.globalConfigProperties = globalConfigProperties;
    }

    public void runFromFramework(String... args) throws Exception {
        run(ARGUMENT_OPTION_FRAMEWORK, true, args);
    }

    public void runFromProject(String... args) throws Exception {
        loggerService.info("Arguments : "+ Arrays.asList(args));
        run(ARGUMENT_OPTION_PROJECT, false, args);
    }

    public void run(List<ArgumentOption> argumentOptionList, boolean loadProject, String... args) throws Exception {
        List<String> argumentListForProject = getArgumentsForProject(args);
        String[] argumentsListSeparatedByOptionAndValue = getArgumentsSeparatedByOptionAndValue(argumentListForProject);
        CommandLine commandLine = ArgumentParser.getOption(argumentsListSeparatedByOptionAndValue, ArgumentParser.getOptionList(argumentOptionList));
        if (loadProject) {
            String projectPath = commandLine.getOptionValue(ArgumentOption.PROJECT.getOption());
            loadProject(projectPath);
        }
        runTestSuite(commandLine);
    }

    private List<String> getArgumentsForProject(String[] args) {
        List<String> filterArguments = Arrays.stream(args).filter(arg ->  ArgumentOption.isOptionForProject(arg)).collect(Collectors.toList());
        loggerService.info("Arguments after filter : "+filterArguments);
        return filterArguments;
    }

    private String[] getArgumentsSeparatedByOptionAndValue(List<String> filterArguments) {
        String[] argumentsForProject = ArgumentParser.splitArguments(filterArguments, IConstant.SEPARATOR_ARG, 2);
        loggerService.info("Arguments after decomposition : "+Arrays.asList(argumentsForProject));
        return argumentsForProject;
    }

    private void loadProject(String projectPath) throws WebEngineException {
        loggerService.info("Loading project : " + projectPath + " is running");
        JarUtil.loadLibrary(new File(projectPath));
        loggerService.info("Loading project : " + projectPath + " is succeed");
    }

    private void runTestSuite(CommandLine commandLine) throws WebEngineException, IOException {
        TestSuiteData testSuiteData = TestSuiteHelper.getTestSuiteData(commandLine);
        Settings settings = TestSuiteHelper.getSettings(commandLine, globalConfigProperties);
        EnvironmentVariables environmentVariables = TestSuiteHelper.getEnvironmentVariables(commandLine);
        ITestSuite testSuite = TestSuiteHelper.getTestSuite();
        Map<String, TestCaseAdditionalInformation> testCaseAdditionalInformationMap = TestSuiteHelper.getTestCaseAdditionalInformation(testSuite, testSuiteData);

        GlobalApplicationContext globalApplicationContext = GlobalApplicationContext.builder()
                .settings(settings)
                .environmentVariables(environmentVariables)
                .testSuiteData(testSuiteData)
                .testCaseAdditionnalInformationList(testCaseAdditionalInformationMap)
                .build();

        loggerService.info("Start Phase initialize test suite ");
        testSuiteExecutor.initialize(globalApplicationContext);
        loggerService.info("End Phase initialize ");

        loggerService.info("Start run test ");
        if (testSuite instanceof AbstractTestSuite) {
            ((AbstractTestSuite) testSuite).setGlobalApplicationContext(globalApplicationContext);
        }
        TestSuiteReport testSuiteReport = testSuiteExecutor.run(globalApplicationContext, testSuite);
        loggerService.info("End run test ");

        loggerService.info("Start clean ");
        testSuiteExecutor.cleanUp(globalApplicationContext);
        loggerService.info("End clean ");

        loggerService.info("Start report ");
        reportHelper.generateAllReport(testSuiteReport, testSuite.getClass().getSimpleName(), settings.getLogDir());
        loggerService.info("End report ");
    }
}
