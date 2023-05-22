package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.core.AbstractTestSuite;
import fr.axa.automation.webengine.core.IAction;
import fr.axa.automation.webengine.core.ITestCase;
import fr.axa.automation.webengine.core.ITestStep;
import fr.axa.automation.webengine.core.ITestSuite;
import fr.axa.automation.webengine.core.TestCaseAdditionalInformation;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Browser;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.general.Settings;
import fr.axa.automation.webengine.generated.EnvironmentVariables;
import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.generated.TestSuiteData;
import fr.axa.automation.webengine.generated.Variable;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.util.CommonClassUtil;
import fr.axa.automation.webengine.util.FileUtil;
import fr.axa.automation.webengine.util.XmlUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


public final class TestSuiteHelper {

    private TestSuiteHelper() {
    }

    private static final LoggerService loggerService = LoggerServiceProvider.getInstance();

    public static ITestSuite getTestSuite() throws WebEngineException {
        Set<Class<? extends ITestSuite>> testSuiteList = getTestSuiteList();
        ITestSuite testSuite = filterTestSuite(testSuiteList);
        if (testSuite == null) {
            throw new WebEngineException("TestSuite class is null. No TestSuite class found in the project");
        }
        return testSuite;
    }

    private static Set<Class<? extends ITestSuite>> getTestSuiteList() {
        loggerService.info("Find Test Suite Class is running ");
        Set<Class<? extends ITestSuite>> testSuiteList = CommonClassUtil.findAllClass(ITestSuite.class);
        loggerService.info("Find Test Suite Class is succeed. Class founded is : " + testSuiteList.toString());
        return testSuiteList;
    }

    private static ITestSuite filterTestSuite(Set<Class<? extends ITestSuite>> testSuiteList) throws WebEngineException {
        ITestSuite testSuite = null;
        if(CollectionUtils.isNotEmpty(testSuiteList)){
            Optional<Class<? extends ITestSuite>> clazz = testSuiteList.stream().filter(ts -> !ts.getSimpleName().equalsIgnoreCase(AbstractTestSuite.class.getSimpleName())).findFirst();
            if(clazz.isPresent()) {
                testSuite = CommonClassUtil.create(clazz.get());
            }
        }
        return testSuite;
    }

    public static EnvironmentVariables getEnvironmentVariables(CommandLine cmd) throws WebEngineException {
        String environmentVariablesFilePath = cmd.getOptionValue(ArgumentOption.ENVIRONMENT_VARIABLE.getOption());
        loggerService.info("Loading environment data running: " + environmentVariablesFilePath);
        EnvironmentVariables environmentVariables = XmlUtil.unmarshall(environmentVariablesFilePath, EnvironmentVariables.class);
        loggerService.info("Loading environment data is succeed: " + environmentVariablesFilePath);
        return environmentVariables;
    }

    public static TestSuiteData getTestSuiteData(CommandLine cmd) throws WebEngineException {
        String testDataFile = cmd.getOptionValue(ArgumentOption.TEST_DATA.getOption());
        loggerService.info("Loading test data running: " + testDataFile);
        TestSuiteData testSuiteData = XmlUtil.unmarshall(testDataFile, TestSuiteData.class);
        loggerService.info("Loading test data is succeed " + testDataFile);
        return testSuiteData;
    }

    public static Settings getSettings(CommandLine cmd, GlobalConfigProperties globalConfigProperties) throws WebEngineException {
        loggerService.info("Loading settings running ");
        List<String> propertiesFileList = getPropertiesFiles(cmd);
        Platform platform = getPlatform(cmd, globalConfigProperties);
        Browser browser = getBrowser(cmd, globalConfigProperties);
        List<String> browserOptionsList = getBrowserOptionList(globalConfigProperties);
        String outputDir = getOutputDir(cmd, globalConfigProperties);
        List<String> testCaseToRunList = getTestCaseToRunList(cmd);

        Settings settings = Settings.builder().propertiesFileList(propertiesFileList).platform(platform).browser(browser).browserOptionsList(browserOptionsList).testCaseToRunList(testCaseToRunList).logDir(outputDir).build();
        loggerService.info("Loading settings running is succeed : " + settings.toString());
        return settings;
    }

    private static List<String> getArgumentList(CommandLine cmd, ArgumentOption argumentOption) {
        List<String> propertiesFileList = new ArrayList<>();
        String propertiesFiles = cmd.getOptionValue(argumentOption.getOption());
        if (propertiesFiles != null) {
            propertiesFileList = Arrays.asList(propertiesFiles.split(";"));
        }
        return propertiesFileList;
    }

    private static List<String> getPropertiesFiles(CommandLine cmd) {
        return getArgumentList(cmd,ArgumentOption.PROPERTIES_FILE_LIST);
    }

    private static List<String> getTestCaseToRunList(CommandLine cmd){
        return getArgumentList(cmd,ArgumentOption.TEST_CASE_TO_RUN);
    }

    private static Platform getPlatform(CommandLine cmd, GlobalConfigProperties globalConfigProperties) throws WebEngineException {
        String platform = cmd.getOptionValue(ArgumentOption.PLATFORM.getOption());
        if (platform == null) {
            if (globalConfigProperties != null && StringUtils.isNotEmpty(globalConfigProperties.getPlateform())) {
                return PlatformTypeHelper.getPlatform(globalConfigProperties.getPlateform());
            } else {
                return Platform.getDefaultPlatform();
            }
        } else {
            return PlatformTypeHelper.getPlatform(platform);
        }
    }

    private static Browser getBrowser(CommandLine cmd, GlobalConfigProperties globalConfigProperties) throws WebEngineException {
        String browser = cmd.getOptionValue(ArgumentOption.BROWSER.getOption());
        if (browser == null) {
            if (globalConfigProperties != null && StringUtils.isNotEmpty(globalConfigProperties.getBrowser())) {
                return BrowserTypeHelper.getBrowser(globalConfigProperties.getBrowser());
            } else {
                return Browser.getDefaultBrowser();
            }
        } else {
            return BrowserTypeHelper.getBrowser(browser);
        }
    }

    private static List<String> getBrowserOptionList(GlobalConfigProperties globalConfigProperties) {
        if (globalConfigProperties != null && globalConfigProperties.getApplication()!=null && CollectionUtils.isNotEmpty(globalConfigProperties.getApplication().getBrowserOptionList())) {
            return globalConfigProperties.getApplication().getBrowserOptionList();
        }
        return Collections.emptyList();
    }

    private static String getOutputDir(CommandLine cmd, GlobalConfigProperties globalConfigProperties) {
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

    public static Map<String, TestCaseAdditionalInformation> getTestCaseAdditionalInformation(ITestSuite testSuite, TestSuiteData testSuiteData) throws WebEngineException {
        Map<String, TestCaseAdditionalInformation> map = new HashMap<>();
        if (testSuite != null) {
            for (AbstractMap.SimpleEntry<String, ? extends ITestCase> entry : testSuite.getTestCaseList()) {
                String testCaseName = entry.getKey();
                List<? extends ITestStep> testStepList = entry.getValue().getTestStepList();
                for (ITestStep testStep : testStepList) {
                    map.put(testCaseName, getTestCaseAdditionalInformation(testSuiteData.getTestDatas(), testCaseName, testStep));
                }
            }
        }
        return map;
    }

    private static TestCaseAdditionalInformation getTestCaseAdditionalInformation(List<TestData> testDataList, String testCaseName, ITestStep testStep) throws WebEngineException {
        IAction action = CommonClassUtil.create(testStep.getAction());
        List<Variable> requiredParametersList = action.getRequiredParameters();
        List<Variable> additionalDataList = new ArrayList<>();
        List<Variable> missingDataList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(requiredParametersList) && CollectionUtils.isNotEmpty(testDataList)) {
            for (Variable requiredParameter : requiredParametersList) {
                Variable variableFound = TestDataHelper.getVariableOfTestCase(testDataList, testCaseName, requiredParameter.getName());
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
}
