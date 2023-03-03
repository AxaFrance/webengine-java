package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.Browser;
import fr.axa.automation.webengine.global.Platform;
import fr.axa.automation.webengine.global.Settings;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.util.FileUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public abstract class AbstractTestSuiteHelper {

    protected static final LoggerService loggerService = LoggerServiceProvider.getInstance();

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

    protected static List<String> getArgumentList(CommandLine cmd, ArgumentOption argumentOption) {
        List<String> propertiesFileList = new ArrayList<>();
        String propertiesFiles = cmd.getOptionValue(argumentOption.getOption());
        if (propertiesFiles != null) {
            propertiesFileList = Arrays.asList(propertiesFiles.split(";"));
        }
        return propertiesFileList;
    }

    protected static List<String> getPropertiesFiles(CommandLine cmd) {
        return getArgumentList(cmd,ArgumentOption.PROPERTIES_FILE_LIST);
    }

    protected static List<String> getTestCaseToRunList(CommandLine cmd){
        return getArgumentList(cmd,ArgumentOption.TEST_CASE_TO_RUN);
    }

    protected static Platform getPlatform(CommandLine cmd, GlobalConfigProperties globalConfigProperties) throws WebEngineException {
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

    protected static Browser getBrowser(CommandLine cmd, GlobalConfigProperties globalConfigProperties) throws WebEngineException {
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

    protected static List<String> getBrowserOptionList(GlobalConfigProperties globalConfigProperties) {
        if (globalConfigProperties != null && globalConfigProperties.getApplication()!=null && CollectionUtils.isNotEmpty(globalConfigProperties.getApplication().getBrowserOptionList())) {
            return globalConfigProperties.getApplication().getBrowserOptionList();
        }
        return Collections.emptyList();
    }

    protected static String getOutputDir(CommandLine cmd, GlobalConfigProperties globalConfigProperties) {
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
}
