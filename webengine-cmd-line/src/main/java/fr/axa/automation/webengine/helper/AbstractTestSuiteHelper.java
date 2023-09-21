package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.Browser;
import fr.axa.automation.webengine.global.Platform;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.report.constante.ReportPathConstant;
import fr.axa.automation.webengine.util.FileUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public abstract class AbstractTestSuiteHelper {

    protected static final LoggerService loggerService = LoggerServiceProvider.getInstance();

    protected static List<String> getArgumentList(CommandLine cmd, ArgumentOption argumentOption) {
        List<String> argumentList = new ArrayList<>();
        String argument = cmd.getOptionValue(argumentOption.getOption());
        if (argument != null && argument.contains("[") && argument.contains("]")) {
            argumentList = Arrays.asList(argument);
        }else if(argument != null){
            argumentList = Arrays.asList(argument.split(";"));
        }
        return argumentList;
    }

    protected static List<String> getPropertiesFiles(CommandLine cmd) {
        return getArgumentList(cmd,ArgumentOption.PROPERTIES_FILE_LIST);
    }

    protected static Platform getPlatform(CommandLine cmd, GlobalConfiguration globalConfiguration) throws WebEngineException {
        String platform = cmd.getOptionValue(ArgumentOption.PLATFORM.getOption());
        if (platform == null) {
            if (globalConfiguration != null && StringUtils.isNotEmpty(globalConfiguration.getPlateform())) {
                return PlatformTypeHelper.getPlatform(globalConfiguration.getPlateform());
            } else {
                return Platform.getDefaultPlatform();
            }
        } else {
            return PlatformTypeHelper.getPlatform(platform);
        }
    }

    protected static Browser getBrowser(CommandLine cmd, GlobalConfiguration globalConfiguration) throws WebEngineException {
        String browser = cmd.getOptionValue(ArgumentOption.BROWSER.getOption());
        if (browser == null) {
            if (globalConfiguration != null && StringUtils.isNotEmpty(globalConfiguration.getBrowser())) {
                return BrowserTypeHelper.getBrowser(globalConfiguration.getBrowser());
            } else {
                return Browser.getDefaultBrowser();
            }
        } else {
            return BrowserTypeHelper.getBrowser(browser);
        }
    }

    protected static Map<String,String> getValues(GlobalConfiguration globalConfiguration) throws WebEngineException {
        if (globalConfiguration != null && globalConfiguration.getApplicationConfiguration()!=null && MapUtils.isNotEmpty(globalConfiguration.getApplicationConfiguration().getValues())) {
            return globalConfiguration.getApplicationConfiguration().getValues();
        }
        return new HashMap<>();
    }


    protected static List<String> getBrowserOptionList(GlobalConfiguration globalConfiguration) {
        if (globalConfiguration != null && globalConfiguration.getWebengineConfiguration()!=null && CollectionUtils.isNotEmpty(globalConfiguration.getWebengineConfiguration().getBrowserOptionList())) {
            return globalConfiguration.getWebengineConfiguration().getBrowserOptionList();
        }
        return Collections.emptyList();
    }

    protected static String getOutputDir(CommandLine cmd, GlobalConfiguration globalConfiguration) {
        String outputDir = cmd.getOptionValue(ArgumentOption.OUTPUT_DIR.getOption());
        if (outputDir != null) {
            outputDir += File.separator;
        } else {
            if (globalConfiguration !=null) {
                outputDir = globalConfiguration.getOutputDir();
            }
            if (StringUtils.isEmpty(outputDir)) {
                outputDir = FileUtil.getPathInTargetDirectory(ReportPathConstant.REPORT_DIRECTORY_NAME.getValue());
            }
        }
        return outputDir;
    }

    public static Boolean getCloseBrowser(CommandLine cmd) {
        return Optional.ofNullable(cmd.getOptionValue(ArgumentOption.CLOSE_BROWSER_AFTER_EACH_SCENARIO.getOption()))
                .map(Boolean::parseBoolean)
                .orElse(true);
    }
}
