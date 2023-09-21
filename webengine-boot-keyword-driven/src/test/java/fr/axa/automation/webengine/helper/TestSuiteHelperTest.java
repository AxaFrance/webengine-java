package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.core.ITestSuite;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.EnvironmentVariables;
import fr.axa.automation.webengine.generated.TestSuiteData;
import fr.axa.automation.webengine.global.Browser;
import fr.axa.automation.webengine.global.Platform;
import fr.axa.automation.webengine.global.Settings;
import fr.axa.automation.webengine.parser.ArgumentParser;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.util.FileUtil;
import fr.axa.automation.webengine.util.JarUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

class TestSuiteHelperTest {

    private static final List<ArgumentOption> ARGUMENT_OPTION_PROJECT_FROM_CONFIG_FILE = Arrays.asList(ArgumentOption.TEST_DATA, ArgumentOption.ENVIRONMENT_VARIABLE);
    private static final List<ArgumentOption> ARGUMENT_OPTION_PROJECT_FROM_CONFIG_COMMAND_LINE = Arrays.asList(ArgumentOption.BROWSER, ArgumentOption.PLATFORM, ArgumentOption.TEST_DATA, ArgumentOption.ENVIRONMENT_VARIABLE, ArgumentOption.PROPERTIES_FILE_LIST);

    @Test
    void getTestSuite() throws URISyntaxException, WebEngineException {
        JarUtil.loadLibrary(new File(FileUtil.getFileFromResource("jar/spoolnet-automation-keyword-driven-1.0-SNAPSHOT.jar").toURI()));
        ITestSuite testSuite = TestSuiteHelper.getTestSuite();
        Assertions.assertNotNull(testSuite);
    }

    @Test
    void getEnvironmentVariables() throws WebEngineException, ParseException {
        String []  argumentsListSeparatedByOptionAndValue = new String[]{"-data","input/data.xml","-env","input/env.xml"};
        CommandLine commandLine = ArgumentParser.getOption(argumentsListSeparatedByOptionAndValue, ArgumentParser.getOptionList(ARGUMENT_OPTION_PROJECT_FROM_CONFIG_FILE));
        EnvironmentVariables environmentVariables = TestSuiteHelper.getEnvironmentVariables(commandLine);
        Assertions.assertEquals("https://axa.fr",EnvironmentVariablesHelper.getEnvironnementValue("URL_RECETTE",environmentVariables.getVariables()).get().getValue());
    }

    @Test
    void getTestSuiteData() throws WebEngineException, ParseException {
        String []  argumentsListSeparatedByOptionAndValue = new String[]{"-data","input/data.xml","-env","input/env.xml"};
        CommandLine commandLine = ArgumentParser.getOption(argumentsListSeparatedByOptionAndValue, ArgumentParser.getOptionList(ARGUMENT_OPTION_PROJECT_FROM_CONFIG_FILE));
        TestSuiteData testSuiteData = TestSuiteHelper.getTestSuiteData(commandLine);
        Assertions.assertEquals("Second test case", TestDataHelper.getVariableOfTestCase(testSuiteData.getTestDatas(),"TEST_CASE_2","TEST_CASE_DESCRIPTION").getValue());
    }

    @Test
    void getSettingsFromCmdLIne() throws WebEngineException, ParseException {
        GlobalConfiguration globalConfiguration = null;
        String []  argumentsListSeparatedByOptionAndValue = new String[]{"-platform","WINDOWS","-browser","CHROME","-data","input/data.xml","-env","input/env.xml"};
        CommandLine commandLine = ArgumentParser.getOption(argumentsListSeparatedByOptionAndValue, ArgumentParser.getOptionList(ARGUMENT_OPTION_PROJECT_FROM_CONFIG_COMMAND_LINE));
        Settings settings = TestSuiteHelper.getSettings(commandLine, globalConfiguration);
        Assertions.assertEquals(Browser.CHROME,settings.getBrowser());
        Assertions.assertEquals(Platform.WINDOWS,settings.getPlatform());
    }

    @Test
    void getSettingsFromConfigFile() throws WebEngineException, ParseException {
        GlobalConfiguration globalConfiguration = null;
        String []  argumentsListSeparatedByOptionAndValue = new String[]{"CHROME","-data","input/data.xml","-env","input/env.xml"};
        CommandLine commandLine = ArgumentParser.getOption(argumentsListSeparatedByOptionAndValue, ArgumentParser.getOptionList(ARGUMENT_OPTION_PROJECT_FROM_CONFIG_FILE));
        Settings settings = TestSuiteHelper.getSettings(commandLine, globalConfiguration);
        Assertions.assertEquals(Browser.CHROMIUM_EDGE,settings.getBrowser());
        Assertions.assertEquals(Platform.WINDOWS,settings.getPlatform());
    }
}