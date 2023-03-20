package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.SettingsDriveByExcel;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.util.RegexUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class TestSuiteHelperDriveByExcel extends AbstractTestSuiteHelper {

    public static final String TEST_CASE_AND_DATA_TEST_COLUMN_NAME_PATTERN = "([^;].*?\\[.*?])?"; // "-tc:firsttestcase[-dataNameColum:jdd-rec-auto;jdd-rec-moto];testcase2[-dataNameColum:jdd-rec-moto]"
    public static final String TEST_CASE_PATTERN = "\\w*\\[";

    public static SettingsDriveByExcel getSettings(CommandLine cmd, GlobalConfigProperties globalConfigProperties) throws WebEngineException {
        loggerService.info("Loading settings running ");
        SettingsDriveByExcel settings = SettingsDriveByExcel.builder()
                .fileName(getFileName(cmd))
                .propertiesFileList(getPropertiesFiles(cmd))
                .platform(getPlatform(cmd, globalConfigProperties))
                .browser(getBrowser(cmd, globalConfigProperties))
                .browserOptionsList(getBrowserOptionList(globalConfigProperties))
                .testCaseAndDataTestColumName(getTestCaseAndDataTestColumnName(cmd))
                .logDir(getOutputDir(cmd, globalConfigProperties))
                .build();
        loggerService.info("Loading settings running is succeed : " + settings.toString());
        return settings;
    }

    public static String getFileName(CommandLine cmd) throws WebEngineException {
        return cmd.getOptionValue(ArgumentOption.TEST_DATA.getOption());
    }

    protected static Map<String, List<String>> getTestCaseAndDataTestColumnName(CommandLine cmd){
        List<String> argumentList = getArgumentList(cmd, ArgumentOption.TEST_CASE_AND_DATA_TEST_COLUMN_NAME); // "-tc:firsttestcase[-dataNameColum:jdd-rec-auto;jdd-rec-moto];testcase2[-dataNameColum:jdd-rec-moto]"
        Map<String, List<String>> testCaseAndDataTestColumName =new HashMap<>() ;
        for (String argument : argumentList) {
            Set<String> testCaseAndDataTestColumnSet = RegexUtil.match(TEST_CASE_AND_DATA_TEST_COLUMN_NAME_PATTERN,argument);
            for (String testCaseAndDataTestColumn:testCaseAndDataTestColumnSet) { //firsttestcase[-dataNameColum:jdd-rec-auto;jdd-rec-moto]
                String testCase = getTestCase(testCaseAndDataTestColumn);
                List<String> dataTestColumnNameList = getDataTestColumnName(testCaseAndDataTestColumn);
                testCaseAndDataTestColumName.put(testCase,dataTestColumnNameList);
            }
        }
        return testCaseAndDataTestColumName;
    }

    private static String getTestCase(String testCaseAndDataTestColumn) {
        Set<String> testCaseSet  = RegexUtil.match(TEST_CASE_PATTERN, testCaseAndDataTestColumn);
        String testCase = "";
        if(CollectionUtils.isEmpty(testCaseSet)){
            testCase = testCaseAndDataTestColumn;
        }else{
            Optional<String> testCaseOptionale = testCaseSet.stream().findFirst();
            if(testCaseOptionale.isPresent()){
                testCase = testCaseOptionale.get();
                testCase = testCase.substring(0,testCase.length()-1);
            }
        }
        return testCase;
    }

    private static List<String> getDataTestColumnName(String testCaseAndDataTestColumn) {
        List<String> dataTestColumnList = new ArrayList<>();
        Set<String> dataTestColumnMatchSet  = RegexUtil.match("\\[([^\\)]+)\\]", testCaseAndDataTestColumn);
        if(CollectionUtils.isNotEmpty(dataTestColumnMatchSet)){
            Optional<String> dataTestColumnOptional = dataTestColumnMatchSet.stream().findFirst();
            if (dataTestColumnOptional.isPresent()){
                Arrays.asList(dataTestColumnOptional.get().split(":")[0].split(";"));
            }
        }
       return dataTestColumnList;
    }

}
