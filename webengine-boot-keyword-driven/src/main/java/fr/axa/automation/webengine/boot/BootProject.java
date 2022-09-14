package fr.axa.automation.webengine.boot;

import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.argument.ArgumentParser;
import fr.axa.automation.webengine.constante.IConstant;
import fr.axa.automation.webengine.core.*;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.general.Settings;
import fr.axa.automation.webengine.generated.*;
import fr.axa.automation.webengine.helper.BrowserTypeHelper;
import fr.axa.automation.webengine.helper.PlatformTypeHelper;
import fr.axa.automation.webengine.helper.TestSuiteHelper;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.report.helper.ReportHelper;
import fr.axa.automation.webengine.util.ClassUtil;
import fr.axa.automation.webengine.util.FileUtil;
import fr.axa.automation.webengine.util.JarUtil;
import fr.axa.automation.webengine.util.XmlUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class BootProject {

    private static final List<ArgumentOption> ARGUMENT_OPTION_FRAMEWORK = Arrays.asList(ArgumentOption.PROJECT,ArgumentOption.TEST_DATA,ArgumentOption.ENVIRONNEMENT_VARIABLE,ArgumentOption.BROWSER, ArgumentOption.PLATFORM, ArgumentOption.OUTPUT_DIR, ArgumentOption.MANUAL_DEBUG, ArgumentOption.JUNIT, ArgumentOption.SHOW_REPORT);
    private static final List<ArgumentOption> ARGUMENT_OPTION_PROJECT = Arrays.asList(ArgumentOption.TEST_DATA,ArgumentOption.ENVIRONNEMENT_VARIABLE,ArgumentOption.BROWSER, ArgumentOption.PLATFORM,ArgumentOption.OUTPUT_DIR, ArgumentOption.MANUAL_DEBUG, ArgumentOption.JUNIT, ArgumentOption.SHOW_REPORT);

    final LoggerService loggerService;

    final ITestSuiteExecutor testSuiteExecutor;

    final ReportHelper reportHelper;

    @Autowired
    public BootProject(LoggerService loggerService, ITestSuiteExecutor testSuiteExecutor, ReportHelper reportHelper) {
        this.loggerService = loggerService;
        this.testSuiteExecutor = testSuiteExecutor;
        this.reportHelper = reportHelper;
    }

    public void runFromFramework(String... args) throws Exception {
        String[] newArgs = ArgumentParser.splitArguments(args, IConstant.SEPARATOR_ARG,2);
        CommandLine commandLine = ArgumentParser.getOption(newArgs, ArgumentParser.getOptionList(ARGUMENT_OPTION_FRAMEWORK));
        loadProject(commandLine);
        runTestSuite(commandLine,newArgs);
    }

    public void runFromProject(String... args) throws Exception {
        String[] newArgs = ArgumentParser.splitArguments(args, IConstant.SEPARATOR_ARG,2);
        CommandLine commandLine = ArgumentParser.getOption(newArgs, ArgumentParser.getOptionList(ARGUMENT_OPTION_PROJECT));
        runTestSuite(commandLine,newArgs);
    }

    private void runTestSuite(CommandLine commandLine,String[] args) throws WebEngineException, IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {
        TestSuiteData testSuiteData = getTestSuiteData(commandLine);
        Settings settings = getSettings(commandLine);
        EnvironmentVariables environmentVariables = getEnvironmentVariables(commandLine);
        ITestSuite testSuite = getTestSuiteExecutor(args);
        Map<String, TestCaseAdditionalInformation> testCaseAdditionalInformationMap = getTestCaseAdditionalInformation(testSuite,testSuiteData);

        GlobalApplicationContext globalApplicationContext = GlobalApplicationContext.builder()
                .settings(settings)
                .environmentVariables(environmentVariables)
                .testSuiteData(testSuiteData)
                .testCaseAdditionnalInformationList(testCaseAdditionalInformationMap)
                .build();

        loggerService.info("Start Phase initialize ");
        testSuiteExecutor.initialize(globalApplicationContext);
        loggerService.info("End Phase initialize ");

        loggerService.info("Start run test ");
        if(testSuite instanceof AbstractTestSuite){
            ((AbstractTestSuite) testSuite).setGlobalApplicationContext(globalApplicationContext);
        }
        TestSuiteReport testSuiteReport = testSuiteExecutor.run(globalApplicationContext,testSuite);
        loggerService.info("End run test ");

        loggerService.info("Start clean ");
        testSuiteExecutor.cleanUp(globalApplicationContext);
        loggerService.info("End clean ");

        loggerService.info("Start report ");
        reportHelper.generateAllReport(testSuiteReport,testSuite.getClass().getSimpleName(),settings.getLogDir());
        loggerService.info("End report ");
    }

    private Map<String,TestCaseAdditionalInformation> getTestCaseAdditionalInformation(ITestSuite testSuite, TestSuiteData testSuiteData ) throws WebEngineException {
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

    private TestCaseAdditionalInformation getTestCaseAdditionalInformation(List<TestData> testDataList, String testCaseName, ITestStep testStep) throws WebEngineException {
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

    private void loadProject(CommandLine cmd) throws WebEngineException {
        String projectPath = cmd.getOptionValue(ArgumentOption.PROJECT.getOption());
        loggerService.info("Loading project : " + projectPath +" is running");
        JarUtil.loadLibrary(new File(projectPath));
        loggerService.info("Loading project : " + projectPath +" is succeed");
    }

    private ITestSuite getTestSuiteExecutor(String... args) throws  WebEngineException{
        Set<Class<? extends ITestSuite>> testSuiteList = getTestSuiteList();
        ITestSuite testSuite = TestSuiteHelper.getTestSuite(testSuiteList);
        if(testSuite==null){
            throw new WebEngineException("TestSuite class is null. No TestSuite class found in the project");
        }
        return testSuite;
    }

    private Set<Class<? extends ITestSuite>> getTestSuiteList() {
        loggerService.info("Find Test Suite Class is running ");
        Set<Class<? extends ITestSuite>> testSuite = JarUtil.findAllClass(ITestSuite.class);
        loggerService.info("Test Suite class founded is : "+testSuite.toString());
        loggerService.info("Find Test Suite Class is succeed ");
        return testSuite;
    }

    private EnvironmentVariables getEnvironmentVariables(CommandLine cmd) throws WebEngineException {
        String environnementVariablesFilePath = cmd.getOptionValue(ArgumentOption.ENVIRONNEMENT_VARIABLE.getOption());
        loggerService.info("Loading environment data running: " + environnementVariablesFilePath);
        EnvironmentVariables environmentVariables = XmlUtil.unmarshall(environnementVariablesFilePath,EnvironmentVariables.class);
        loggerService.info("Loading environment data succeed: " + environnementVariablesFilePath);
        return environmentVariables;
    }

    private TestSuiteData getTestSuiteData(CommandLine cmd) throws WebEngineException {
        String testData = cmd.getOptionValue(ArgumentOption.TEST_DATA.getOption());
        loggerService.info("Loading test data running: " + testData);
        TestSuiteData testSuiteData = XmlUtil.unmarshall(testData, TestSuiteData.class);
        loggerService.info("Loading test data is succeed " + testData);
        return testSuiteData;
    }

    private Settings getSettings(CommandLine cmd) throws WebEngineException {
        loggerService.info("Loading settings running ");
        String browser = cmd.getOptionValue(ArgumentOption.BROWSER.getOption());
        String platform = cmd.getOptionValue(ArgumentOption.PLATFORM.getOption());
        String outputDir = cmd.getOptionValue(ArgumentOption.OUTPUT_DIR.getOption());
        if(platform==null){
            platform = Platform.WINDOWS.getValue();
        }
        if(outputDir!=null){
            outputDir += File.separator;
        }else{
            outputDir = FileUtil.getDefaultRunResultDirectory();
        }

        Settings settings = Settings.builder().platform(PlatformTypeHelper.getPlatform(platform)).browser(BrowserTypeHelper.getBrowser(browser)).logDir(outputDir).build();
        loggerService.info("Loading settings running is succeed : "+settings.toString());
        return settings;
    }
}
