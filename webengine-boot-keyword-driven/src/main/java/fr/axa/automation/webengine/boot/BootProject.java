package fr.axa.automation.webengine.boot;

import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.argument.ArgumentParser;
import fr.axa.automation.webengine.constante.IConstant;
import fr.axa.automation.webengine.core.*;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Browser;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.general.Settings;
import fr.axa.automation.webengine.generated.*;
import fr.axa.automation.webengine.helper.BrowserTypeHelper;
import fr.axa.automation.webengine.helper.PlatformTypeHelper;
import fr.axa.automation.webengine.helper.TestSuiteHelper;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.report.helper.global.IReportHelper;
import fr.axa.automation.webengine.util.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;
import java.util.stream.Collectors;


@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class BootProject {

    private static final List<ArgumentOption> ARGUMENT_OPTION_FRAMEWORK = Arrays.asList(ArgumentOption.PROJECT, ArgumentOption.TEST_DATA, ArgumentOption.ENVIRONMENT_VARIABLE, ArgumentOption.PROPERTIES_FILE_LIST, ArgumentOption.BROWSER, ArgumentOption.PLATFORM, ArgumentOption.OUTPUT_DIR, ArgumentOption.MANUAL_DEBUG, ArgumentOption.JUNIT, ArgumentOption.SHOW_REPORT);

    private static final List<ArgumentOption> ARGUMENT_OPTION_PROJECT = Arrays.asList(ArgumentOption.TEST_DATA, ArgumentOption.ENVIRONMENT_VARIABLE, ArgumentOption.PROPERTIES_FILE_LIST, ArgumentOption.BROWSER, ArgumentOption.PLATFORM, ArgumentOption.OUTPUT_DIR, ArgumentOption.MANUAL_DEBUG, ArgumentOption.JUNIT, ArgumentOption.SHOW_REPORT);

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
        String[] argumentsListSeparatedByOptionAndVaue = getArgumentsSeparatedByOptionAndValue(argumentListForProject);
        CommandLine commandLine = ArgumentParser.getOption(argumentsListSeparatedByOptionAndVaue, ArgumentParser.getOptionList(argumentOptionList));
        if (loadProject) {
            loadProject(commandLine);
        }
        runTestSuite(commandLine);
    }

    private String[] getArgumentsSeparatedByOptionAndValue(List<String> filterArguments) {
        String[] argumentsForProject = ArgumentParser.splitArguments(filterArguments, IConstant.SEPARATOR_ARG, 2);
        loggerService.info("Arguments after decomposition : "+Arrays.asList(argumentsForProject));
        return argumentsForProject;
    }

    private List<String> getArgumentsForProject(String[] args) {
        List<String> filterArguments = Arrays.stream(args).filter(arg ->  ArgumentOption.isOptionForProject(arg)).collect(Collectors.toList());
        loggerService.info("Arguments after filter : "+filterArguments);
        return filterArguments;
    }

    private void runTestSuite(CommandLine commandLine) throws WebEngineException, IOException {
        TestSuiteData testSuiteData = getTestSuiteData(commandLine);
        Settings settings = getSettings(commandLine);
        EnvironmentVariables environmentVariables = getEnvironmentVariables(commandLine);
        ITestSuite testSuite = getTestSuite();
        Map<String, TestCaseAdditionalInformation> testCaseAdditionalInformationMap = getTestCaseAdditionalInformation(testSuite, testSuiteData);

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

    private Map<String, TestCaseAdditionalInformation> getTestCaseAdditionalInformation(ITestSuite testSuite, TestSuiteData testSuiteData) throws WebEngineException {
        Map<String, TestCaseAdditionalInformation> map = new HashMap<>();
        if (testSuite != null) {
            for (AbstractMap.SimpleEntry<String, ? extends ITestCase> entry : testSuite.getTestCaseList()) {
                String testCaseName = entry.getKey();
                List<? extends ITestStep> testStepList = entry.getValue().getTestStepList();
                for (ITestStep testStep : testStepList) {
                    map.put(testCaseName, getTestCaseAdditionalInformation(testSuiteData.getTestData(), testCaseName, testStep));
                }
            }
        }
        return map;
    }

    private TestCaseAdditionalInformation getTestCaseAdditionalInformation(List<TestData> testDataList, String testCaseName, ITestStep testStep) throws WebEngineException {
        IAction action = CommonClassUtil.create(testStep.getAction());
        List<Variable> requiredParametersList = action.getRequiredParameters();
        List<Variable> additionalDataList = new ArrayList<>();
        List<Variable> missingDataList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(requiredParametersList) && CollectionUtils.isNotEmpty(testDataList)) {
            for (Variable requiredParameter : requiredParametersList) {
                Variable variableFound = TestDataUtil.getVariableOfTestCase(testDataList, testCaseName, requiredParameter.getName());
                if (variableFound == null) {
                    if(requiredParameter.getValue() != null){
                        additionalDataList.add(requiredParameter);
                    }else{
                        missingDataList.add(requiredParameter);
                    }
                }
            }
        }
        return TestCaseAdditionalInformation.builder().additionalDataList(additionalDataList).missingDataList(missingDataList).canRun(CollectionUtils.isEmpty(missingDataList)).build();
    }

    private void loadProject(CommandLine cmd) throws WebEngineException {
        String projectPath = cmd.getOptionValue(ArgumentOption.PROJECT.getOption());
        loggerService.info("Loading project : " + projectPath + " is running");
        JarUtil.loadLibrary(new File(projectPath));
        loggerService.info("Loading project : " + projectPath + " is succeed");
    }

    private ITestSuite getTestSuite() throws WebEngineException {
        Set<Class<? extends ITestSuite>> testSuiteList = getTestSuiteList();
        ITestSuite testSuite = TestSuiteHelper.filterTestSuite(testSuiteList);
        if (testSuite == null) {
            throw new WebEngineException("TestSuite class is null. No TestSuite class found in the project");
        }
        return testSuite;
    }

    private Set<Class<? extends ITestSuite>> getTestSuiteList() {
        loggerService.info("Find Test Suite Class is running ");
        Set<Class<? extends ITestSuite>> testSuiteList = JarUtil.findAllClass(ITestSuite.class);
        loggerService.info("Find Test Suite Class is succeed. Class founded is : " + testSuiteList.toString());
        return testSuiteList;
    }

    private EnvironmentVariables getEnvironmentVariables(CommandLine cmd) throws WebEngineException {
        String environmentVariablesFilePath = cmd.getOptionValue(ArgumentOption.ENVIRONMENT_VARIABLE.getOption());
        loggerService.info("Loading environment data running: " + environmentVariablesFilePath);
        EnvironmentVariables environmentVariables = XmlUtil.unmarshall(environmentVariablesFilePath, EnvironmentVariables.class);
        loggerService.info("Loading environment data succeed: " + environmentVariablesFilePath);
        return environmentVariables;
    }

    private TestSuiteData getTestSuiteData(CommandLine cmd) throws WebEngineException {
        String testDataFile = cmd.getOptionValue(ArgumentOption.TEST_DATA.getOption());
        loggerService.info("Loading test data running: " + testDataFile);
        TestSuiteData testSuiteData = XmlUtil.unmarshall(testDataFile, TestSuiteData.class);
        loggerService.info("Loading test data is succeed " + testDataFile);
        return testSuiteData;
    }

    private Settings getSettings(CommandLine cmd) throws WebEngineException {
        loggerService.info("Loading settings running ");
        List<String> propertiesFileList = getPropertiesFiles(cmd);
        String platform = getPlatform(cmd);
        String browser = getBrowser(cmd);
        String outputDir = getOutputDir(cmd);

        Settings settings = Settings.builder().propertiesFileList(propertiesFileList).platform(PlatformTypeHelper.getPlatform(platform)).browser(BrowserTypeHelper.getBrowser(browser)).logDir(outputDir).build();
        loggerService.info("Loading settings running is succeed : " + settings.toString());
        return settings;
    }

    private String getPlatform(CommandLine cmd) {
        String platform = cmd.getOptionValue(ArgumentOption.PLATFORM.getOption());
        if (platform == null) {
            if (globalConfigProperties!=null) {
                platform = globalConfigProperties.getPlateform();
            }
            if (StringUtils.isEmpty(platform)) {
                platform = Platform.getDefaultPlatform().getValue();
            }
        }
        return platform;
    }

    private String getBrowser(CommandLine cmd) {
        String browser = cmd.getOptionValue(ArgumentOption.BROWSER.getOption());
        if (browser == null) {
            if (globalConfigProperties!=null) {
                browser = globalConfigProperties.getBrowser();
            }
            if (StringUtils.isEmpty(browser)) {
                browser = Browser.getDefaultBrowser().getValue();
            }
        }
        return browser;
    }

    private String getOutputDir(CommandLine cmd) {
        String outputDir = cmd.getOptionValue(ArgumentOption.OUTPUT_DIR.getOption());
        if (outputDir != null) {
            outputDir += File.separator;
        } else {
            if (globalConfigProperties!=null) {
                outputDir = globalConfigProperties.getOutputDir();
            }
            if (StringUtils.isEmpty(outputDir)) {
                outputDir = FileUtil.getPathInTargetDirectory(FileUtil.RUN_RESULT_DIRECTORY);
            }
        }
        return outputDir;
    }

    private List<String> getPropertiesFiles(CommandLine cmd) {
        List<String> propertiesFileList = Collections.emptyList();
        String propertiesFiles = cmd.getOptionValue(ArgumentOption.PROPERTIES_FILE_LIST.getOption());
        if (propertiesFiles != null) {
            propertiesFileList = Arrays.asList(propertiesFiles.split(";"));
        }
        return propertiesFileList;
    }
}
