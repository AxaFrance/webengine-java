package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.SettingsNoCode;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.TestCaseDataNoCode;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.util.RegexUtil;
import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public final class TestSuiteHelperNoCode extends AbstractTestSuiteHelper {

    public static final String TEST_CASE_AND_DATA_TEST_COLUMN_NAME_PATTERN = "([.*-]+\\[[-.*:;]+\\])|([.*-]+)";; // "-tc:firsttestcase[-dataColumName:jdd-rec-auto;jdd-rec-moto];testcase2[-dataColumName:jdd-rec-moto]"
    public static final String TEST_CASE_PATTERN = "^([^\\[]+)";
    public static final String DATA_TEST_COLUMN_NAME_PATTERN = "(?<=:)([^\\]]+)";

    public static SettingsNoCode getSettings(CommandLine cmd, GlobalConfiguration globalConfiguration) throws WebEngineException {
        loggerService.info("Loading settings running ");
        SettingsNoCode settings = SettingsNoCode.builder()
                .dataTestFileName(getFileName(cmd))
                .propertiesFileList(getPropertiesFiles(cmd))
                .platform(getPlatform(cmd, globalConfiguration))
                .browser(getBrowser(cmd, globalConfiguration))
                .browserOptionsList(getBrowserOptionList(globalConfiguration))
                .testCaseAndDataTestColumName(getTestCaseAndDataTestColumnName(cmd))
                .values(getValues(globalConfiguration))
                .outputDir(getOutputDir(cmd, globalConfiguration))
                .showReport(getShowReport(cmd))
                .closeBrowser(getCloseBrowser(cmd))
                .keePassDatabasePassword(getKeePassDatabasePassword(cmd))
                .keePassDatabasePath(getKeePassDatabasePath(cmd))
                .deleteTempFile(getDeleteTempFile(cmd))
                .build();
        loggerService.info("Loading settings running is succeed : " + settings.toString());
        return settings;
    }

    private static boolean getDeleteTempFile(CommandLine cmd) {
        return Optional.ofNullable(cmd.getOptionValue(ArgumentOption.DELETE_TEMP_FILE.getOption()))
                .map(Boolean::parseBoolean)
                .orElse(false);
    }

    private static String getKeePassDatabasePath(CommandLine cmd) {
        return cmd.getOptionValue(ArgumentOption.KEEPASS_FILE.getOption());
    }

    private static String getKeePassDatabasePassword(CommandLine cmd) {
        return cmd.getOptionValue(ArgumentOption.KEEPASS_PASSWORD.getOption());
    }

    public static Boolean getShowReport(CommandLine cmd) {
        return Optional.ofNullable(cmd.getOptionValue(ArgumentOption.SHOW_REPORT.getOption()))
                .map(Boolean::parseBoolean)
                .orElse(false);
    }

    public static String getFileName(CommandLine cmd) {
        return cmd.getOptionValue(ArgumentOption.TEST_DATA.getOption());
    }

    public static Map<String, List<String>> getTestCaseAndDataTestColumnName(CommandLine cmd){
        List<String> argumentList = getArgumentList(cmd, ArgumentOption.TEST_CASE_AND_DATA_TEST_COLUMN_NAME); // "-tc:firsttestcase[-dataColumName:jdd-rec-auto;jdd-rec-moto];testcase2[-dataColumName:jdd-rec-moto]"
        Map<String, List<String>> testCaseAndDataTestColumName =new LinkedHashMap<>() ;
        for (String argument : argumentList) {
            List<String> testCaseAndDataTestColumnSet = Arrays.asList(argument.split("]"));
            if(CollectionUtils.isNotEmpty(testCaseAndDataTestColumnSet)){
                for (String testCaseAndDataTestColumn:testCaseAndDataTestColumnSet) { //firsttestcase[-dataColumName:jdd-rec-auto;jdd-rec-moto]
                    if(StringUtil.equalsIgnoreCase(testCaseAndDataTestColumn,";")){
                        continue;
                    }
                    String testCase = getTestCase(testCaseAndDataTestColumn.contains("-tc:")?testCaseAndDataTestColumn.split("-tc:")[1]:testCaseAndDataTestColumn);
                    List<String> dataTestColumnNameList = getDataTestColumnName(testCaseAndDataTestColumn);
                    testCaseAndDataTestColumName.put(testCase.startsWith(";")?testCase.split(";")[1]:testCase,dataTestColumnNameList);
                }
            }else{
                testCaseAndDataTestColumName.put(argument,null);
            }
        }
        return testCaseAndDataTestColumName;
    }

    private static String getTestCase(String testCaseAndDataTestColumn) {
        List<String> testCaseSet  = RegexUtil.match(TEST_CASE_PATTERN, testCaseAndDataTestColumn);
        String testCase = "";
        if(CollectionUtils.isEmpty(testCaseSet)){
            testCase = testCaseAndDataTestColumn;
        }else{
            Optional<String> testCaseOptionale = testCaseSet.stream().findFirst();
            if(testCaseOptionale.isPresent()){
                testCase = testCaseOptionale.get();
            }
        }
        return testCase;
    }

    private static List<String> getDataTestColumnName(String testCaseAndDataTestColumn) {
        List<String> dataTestColumnList = new ArrayList<>();
        List<String> dataTestColumnMatchSet  = RegexUtil.match(DATA_TEST_COLUMN_NAME_PATTERN, testCaseAndDataTestColumn);
        if(CollectionUtils.isNotEmpty(dataTestColumnMatchSet)){
            Optional<String> dataTestColumnOptional = dataTestColumnMatchSet.stream().findFirst();
            if (dataTestColumnOptional.isPresent()){
                dataTestColumnList.addAll(Arrays.asList(dataTestColumnOptional.get().split(";")));
            }
        }
       return dataTestColumnList;
    }

    public static List<CommandDataNoCode> getApplicationVisited(TestCaseDataNoCode testCaseDataNoCode){
        return CommandDataHelper.getCommandDataByName(testCaseDataNoCode, CommandName.OPEN);
    }


    public static TestCaseDataNoCode getTestCaseDataNoCode(TestSuiteDataNoCode testSuiteDataNoCode, String testCaseName){
        return testSuiteDataNoCode.getTestCaseList().stream()
                .filter(testCaseDataNoCode -> StringUtil.equalsIgnoreCase(testCaseDataNoCode.getName(),testCaseName))
                .findFirst()
                .orElse(null);
    }
}
