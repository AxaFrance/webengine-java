package fr.axa.automation.webengine.boot;

import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.argument.ArgumentParser;
import fr.axa.automation.webengine.core.*;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.BrowserType;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.general.Settings;
import fr.axa.automation.webengine.generated.*;
import fr.axa.automation.webengine.helper.ReportHelper;
import fr.axa.automation.webengine.helper.TestSuiteHelper;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.util.ClassUtil;
import fr.axa.automation.webengine.util.JarUtil;
import fr.axa.automation.webengine.util.XmlUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class BootProject {

    private static final List<ArgumentOption> ARGUMENT_OPTION_FRAMEWORK = Arrays.asList(ArgumentOption.PROJECT,ArgumentOption.TEST_DATA,ArgumentOption.ENVIRONNEMENT_VARIABLE,ArgumentOption.BROWSER, ArgumentOption.PLATFORM);
    private static final List<ArgumentOption> ARGUMENT_OPTION_PROJECT = Arrays.asList(ArgumentOption.TEST_DATA,ArgumentOption.ENVIRONNEMENT_VARIABLE,ArgumentOption.BROWSER, ArgumentOption.PLATFORM);

    @Autowired
    LoggerService loggerService;

    @Autowired
    ITestSuiteExecutor testSuiteExecutor;

    @Autowired
    ReportHelper reportHelper;

    public void runFromFramework(String... args) throws Exception {
        CommandLine commandLine = ArgumentParser.getOption(args, ArgumentParser.getOptionList(ARGUMENT_OPTION_FRAMEWORK));
        loadProject(commandLine, args);
        runTestSuite(commandLine,args);
    }

    public void runFromProject(String... args) throws Exception {
        CommandLine commandLine = ArgumentParser.getOption(args, ArgumentParser.getOptionList(ARGUMENT_OPTION_PROJECT));
        runTestSuite(commandLine,args);
    }

    private void runTestSuite(CommandLine commandLine,String[] args) throws WebEngineException, IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {
        TestSuiteData testSuiteData = getTestSuiteData(commandLine,args);
        Settings settings = getSettings(commandLine, args);
        EnvironmentVariables environmentVariables = getEnvironmentVariables(commandLine,args);
        ITestSuite testSuite = getTestSuiteExecutor(args);
        Map<String, TestCaseAdditionalInformation> testCaseAdditionalInformationMap = getTestCaseAdditionalInformation(testSuite,testSuiteData);

        GlobalApplicationContext globalApplicationContext = GlobalApplicationContext.builder()
                .settings(settings)
                .environmentVariables(environmentVariables)
                .testSuite(testSuite)
                .testSuiteData(testSuiteData)
                .testCaseAdditionnalInformationList(testCaseAdditionalInformationMap)
                .build();

        testSuiteExecutor.initialize(globalApplicationContext);
        TestSuiteReport testSuiteReport = testSuiteExecutor.run(globalApplicationContext);
        testSuiteExecutor.cleanUp(globalApplicationContext);
        reportHelper.generateAllReport(testSuiteReport,testSuite.getClass().getSimpleName(),settings.getLogDir());
    }

    private Map<String,TestCaseAdditionalInformation> getTestCaseAdditionalInformation(ITestSuite testSuite, TestSuiteData testSuiteData ) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Map<String,TestCaseAdditionalInformation> map = new HashMap<>();
        if(testSuite!=null){
            List<AbstractMap.SimpleEntry<String, ? extends ITestCase>> testCaseList = testSuite.getTestCaseList();
            for (AbstractMap.SimpleEntry<String, ? extends ITestCase> entry : testCaseList) {
                List<? extends ITestStep> testStepDetailList = entry.getValue().getTestStepList();
                for (ITestStep testStep : testStepDetailList) {
                    map.put(entry.getKey(),getTestCaseAdditionalInformation(testSuiteData.getTestData(),entry.getKey(),testStep));
                }
            }
        }
        return map;
    }

    private TestCaseAdditionalInformation getTestCaseAdditionalInformation(List<TestData> testDataList, String testCaseName, ITestStep testStep) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        IAction action = ClassUtil.create(testStep.getAction());
        List<Variable> requiredParametersList = action.getRequiredParameters();
        List<Variable> additionalDataList = new ArrayList<>();
        List<Variable> missingDataList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(requiredParametersList) && CollectionUtils.isNotEmpty(testDataList)) {
            for (Variable variable : requiredParametersList) {
                Variable variableFound = TestDataUtil.getVariableOfTestCase(testDataList, testCaseName, variable.getName());
                if (variableFound == null && variable.getValue() != null) {
                    additionalDataList.add(variable);
                } else {
                    missingDataList.add(variable);
                }
            }
        }
        return TestCaseAdditionalInformation.builder().additionalDataList(additionalDataList).missingDataList(missingDataList).canRun(CollectionUtils.isEmpty(missingDataList)).build();
    }

    private void loadProject(CommandLine cmd, String... args) throws WebEngineException {
        String projectPath = cmd.getOptionValue(ArgumentOption.PROJECT.getOption());
        loggerService.info("Loading project : " + projectPath +" is running");
        JarUtil.loadLibrary(new File(projectPath));
        loggerService.info("Loading project : " + projectPath +" is succeed");
    }

    private ITestSuite getTestSuiteExecutor(String... args) throws  WebEngineException{
        Set<Class<? extends ITestSuite>> testSuiteList = getTestSuiteList(args);
        ITestSuite testSuite = null;
        try {
            testSuite = TestSuiteHelper.getTestSuite(testSuiteList);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new WebEngineException("No TestSuite class found :",e);
        }
        return testSuite;
    }

    private Set<Class<? extends ITestSuite>> getTestSuiteList(String... args) {
        loggerService.info("Find Test Suite Class is running ");
        return JarUtil.findAllClass(ITestSuite.class);
    }


    private EnvironmentVariables getEnvironmentVariables(CommandLine cmd,String... args) throws WebEngineException {
        String environnementVariablesFilePath = cmd.getOptionValue(ArgumentOption.ENVIRONNEMENT_VARIABLE.getOption());
        loggerService.info("Loading test data running: " + environnementVariablesFilePath);
        EnvironmentVariables environmentVariables = XmlUtil.unmarshall(environnementVariablesFilePath,EnvironmentVariables.class);
        loggerService.info("Loading test data succeed: " + environnementVariablesFilePath);
        return environmentVariables;
    }

    private TestSuiteData getTestSuiteData(CommandLine cmd ,String... args) throws WebEngineException {
        String testData = cmd.getOptionValue(ArgumentOption.TEST_DATA.getOption());
        loggerService.info("Loading test data running: " + testData);
        TestSuiteData testSuiteData = XmlUtil.unmarshall(testData, TestSuiteData.class);
        loggerService.info("Loading test data is succeed " + testData);
        return testSuiteData;
    }

    private Settings getSettings(CommandLine cmd, String... args) throws WebEngineException {
        loggerService.info("Loading settings running ");
        String browser = cmd.getOptionValue(ArgumentOption.BROWSER.getOption());
        String platform = cmd.getOptionValue(ArgumentOption.PLATFORM.getOption());
        String outputDir = cmd.getOptionValue(ArgumentOption.OUTPUT_DIR.getOption());
        if(platform==null){
            platform = Platform.WINDOWS.name();
        }

        Settings settings = Settings.builder().platform(Platform.valueOf(platform)).browserType(BrowserType.valueOf(browser)).logDir(outputDir!=null?outputDir:System.getProperty("java.io.tmpdir")).build();
        loggerService.info("Loading settings running is succeed : "+settings.toString());
        return settings;
    }
}
