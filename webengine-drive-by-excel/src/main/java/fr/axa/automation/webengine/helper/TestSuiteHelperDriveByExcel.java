package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.SettingsDriveByExcel;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.util.RegexUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class TestSuiteHelperDriveByExcel extends AbstractTestSuiteHelper {

    public static final String TEST_CASE_AND_DATA_TEST_COLUMN_NAME_PATTERN = "([\\w-]+\\[[-\\w:;]+\\])|([\\w-]+)";; // "-tc:firsttestcase[-dataColumName:jdd-rec-auto;jdd-rec-moto];testcase2[-dataColumName:jdd-rec-moto]"
    public static final String TEST_CASE_PATTERN = "^([^\\[]+)";
    public static final String DATA_TEST_COLUMN_NAME_PATTERN = "(?<=:)([^\\]]+)";

    public static SettingsDriveByExcel getSettings(CommandLine cmd, GlobalConfiguration globalConfiguration) throws WebEngineException {
        loggerService.info("Loading settings running ");
        SettingsDriveByExcel settings = SettingsDriveByExcel.builder()
                .fileName(getFileName(cmd))
                .propertiesFileList(getPropertiesFiles(cmd))
                .platform(getPlatform(cmd, globalConfiguration))
                .browser(getBrowser(cmd, globalConfiguration))
                .browserOptionsList(getBrowserOptionList(globalConfiguration))
                .testCaseAndDataTestColumName(getTestCaseAndDataTestColumnName(cmd))
                .outputDir(getOutputDir(cmd, globalConfiguration))
                .build();
        loggerService.info("Loading settings running is succeed : " + settings.toString());
        return settings;
    }

    public static String getFileName(CommandLine cmd) {
        return cmd.getOptionValue(ArgumentOption.TEST_DATA.getOption());
    }

    public static Map<String, List<String>> getTestCaseAndDataTestColumnName(CommandLine cmd){
        List<String> argumentList = getArgumentList(cmd, ArgumentOption.TEST_CASE_AND_DATA_TEST_COLUMN_NAME); // "-tc:firsttestcase[-dataColumName:jdd-rec-auto;jdd-rec-moto];testcase2[-dataColumName:jdd-rec-moto]"
        Map<String, List<String>> testCaseAndDataTestColumName =new HashMap<>() ;
        for (String argument : argumentList) {
            List<String> testCaseAndDataTestColumnSet = RegexUtil.match(TEST_CASE_AND_DATA_TEST_COLUMN_NAME_PATTERN,argument);
            if(CollectionUtils.isNotEmpty(testCaseAndDataTestColumnSet)){
                for (String testCaseAndDataTestColumn:testCaseAndDataTestColumnSet) { //firsttestcase[-dataColumName:jdd-rec-auto;jdd-rec-moto]
                    String testCase = getTestCase(testCaseAndDataTestColumn);
                    List<String> dataTestColumnNameList = getDataTestColumnName(testCaseAndDataTestColumn);
                    testCaseAndDataTestColumName.put(testCase,dataTestColumnNameList);
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
}
