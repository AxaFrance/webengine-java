package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.constante.TargetKey;
import fr.axa.automation.webengine.global.ExcelColumn;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.TestCaseDataNoCode;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
import fr.axa.automation.webengine.util.ExcelReader;
import fr.axa.automation.webengine.util.RegexUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ExcelConverter {

    public static final String XPATH_PATTERN = "^([/]{1,2}.*)$";
    public static final String JSON_PATTERN = "^\\{.*\\}$";

    private static final Integer NUMBER_OF_EMPTY_LINE_FOR_ENDING_SCENARIO = 2;

    protected static final ILoggerService loggerService = LoggerServiceProvider.getInstance();

    public static TestSuiteDataNoCode convert(String excelFileName, Map<String, List<String>> testCaseAndDataTestColumNameMap) {
        List<TestCaseDataNoCode> testCaseList = new LinkedList<>();
        Workbook workbook = ExcelReader.getWorkbook(excelFileName);
        List<String> sheetNameList = ExcelReader.getAllSheetName(workbook);
        assertSheetName(sheetNameList,testCaseAndDataTestColumNameMap);
        if (MapUtils.isEmpty(testCaseAndDataTestColumNameMap)) {
            testCaseList.addAll(getTestCaseList(workbook, getTestCaseNameAndDataTestColumnName(sheetNameList)));
        } else {
            testCaseList.addAll(getTestCaseList(workbook, testCaseAndDataTestColumNameMap));
        }

        TestSuiteDataNoCode testSuite = TestSuiteDataNoCode.builder()
                .uid(UUID.randomUUID().toString())
                .name(excelFileName)
                .testCaseList(testCaseList)
                .testCaseNodeList(TreeCreator.createTree(testCaseList))
                .build();
        return testSuite;
    }

    private static void assertSheetName(List<String> sheetNameList,Map<String, List<String>> testCaseAndDataTestColumNameMap){
        List<String> sheetNameDoesntExist = new ArrayList<>();
        if (MapUtils.isNotEmpty(testCaseAndDataTestColumNameMap)) {
            for (String testCaseToRun : testCaseAndDataTestColumNameMap.keySet()) {
                if(!sheetNameList.contains(testCaseToRun)){
                    sheetNameDoesntExist.add(testCaseToRun);
                }
            }
        }
        if(CollectionUtils.isNotEmpty(sheetNameDoesntExist)){
            String messageError = "This test case argument doesn't exist in the excel file";
            loggerService.error(messageError + sheetNameDoesntExist );
            throw new IllegalArgumentException(messageError + sheetNameDoesntExist);
        }
    }

    private static Map<String, List<String>> getTestCaseNameAndDataTestColumnName(List<String> sheetNameList){
        return sheetNameList.stream().distinct().collect(Collectors.toMap(Function.identity(), Arrays::asList));
    }

    private static boolean isEndOfFile(Sheet currentSheet, Integer rowIndex) {
        String cellValue = ExcelReader.getCellValue(currentSheet, rowIndex, ExcelColumn.COMMAND.getValue());
        if(StringUtils.isEmpty(cellValue) || CommandName.fromValue(cellValue) == CommandName.END_SCENARIO){
            return true;
        }
        return false;
    }

    private static List<TestCaseDataNoCode> getTestCaseList(Workbook workbook, Map<String, List<String>> testCaseAndDataTestColumNameMap ) {
        Map<String, TestCaseDataNoCode> testCaseDataMap = new LinkedHashMap<>();
        if (MapUtils.isNotEmpty(testCaseAndDataTestColumNameMap)) {
            for (String testCaseToRun : testCaseAndDataTestColumNameMap.keySet()) {
                if(!testCaseDataMap.containsKey(testCaseToRun)){
                    testCaseDataMap.putAll(getTestCaseList(workbook, testCaseToRun, testCaseAndDataTestColumNameMap.get(testCaseToRun)));
                }
            }
        }
        return new ArrayList<>(testCaseDataMap.values());
    }

    private static Map<String, TestCaseDataNoCode> getTestCaseList(Workbook workbook, String testCaseSheetName, List<String> dataTestColumnNameList) {

        Map<String, TestCaseDataNoCode> testCaseDataMap = new LinkedHashMap<>();
        List<CommandDataNoCode> commandDataList = new LinkedList<>();
        Sheet testCaseSheet = workbook.getSheet(testCaseSheetName);
        int rowIndex = 1;
        boolean endOfFile ;
        int cptEndOfFile = 0;

        while (cptEndOfFile != NUMBER_OF_EMPTY_LINE_FOR_ENDING_SCENARIO) {
            endOfFile = isEndOfFile(testCaseSheet, rowIndex);
            if(endOfFile){
                cptEndOfFile++;
            }else {
                cptEndOfFile=0;
                Row currentRow = testCaseSheet.getRow(rowIndex);
                CommandDataNoCode commandData = CommandDataNoCode.builder()
                        .uid(UUID.randomUUID().toString())
                        .name(getIdValue(currentRow))
                        .command(getCommandValue(currentRow))
                        .targetList(getTargetValueList(currentRow))
                        .optional(getOptionalValue(currentRow))
                        .dataTestReference(getDataTestReferenceValue(currentRow))
                        .dataTestMap(getDataTestList(testCaseSheet, currentRow, dataTestColumnNameList))
                        .build();

                commandDataList.add(commandData);

                if (commandData.getCommand() == CommandName.CALL) {
                    String testCaseNameToCall = commandData.getTargetList().get(TargetKey.CALL);
                    Map<String, TestCaseDataNoCode> testCaseDataCalledMap = getTestCaseList(workbook, testCaseNameToCall, null); //Get all date test column name with the data
                    if (!testCaseDataMap.containsKey(testCaseNameToCall)) {
                        testCaseDataMap.putAll(testCaseDataCalledMap);
                    }
                }
            }
            rowIndex++;
        }

        testCaseDataMap.put(testCaseSheetName, getTestCaseData(testCaseSheet, commandDataList));
        return testCaseDataMap;
    }

    private static TestCaseDataNoCode getTestCaseData(Sheet testCaseSheet, List<CommandDataNoCode> commandDataList) {
        TestCaseDataNoCode testCaseData = TestCaseDataNoCode.builder()
                .uid(UUID.randomUUID().toString())
                .name(testCaseSheet.getSheetName())
                .commandList(commandDataList)
                .build();
        return testCaseData;
    }

    private static String getIdValue(Row currentRow) {
        return ExcelReader.getCellValue(currentRow, ExcelColumn.FIELD_NAME.getValue()).trim();
    }

    private static CommandName getCommandValue(Row currentRow) {
        String commandValue = ExcelReader.getCellValue(currentRow, ExcelColumn.COMMAND.getValue()).trim();
        return CommandName.fromValue(commandValue);

    }

    private static String getOptionalValue(Row currentRow) {
        return ExcelReader.getCellValue(currentRow, ExcelColumn.OPTIONAL.getValue()).trim();
    }

    private static String getDataTestReferenceValue(Row currentRow) {
        return ExcelReader.getCellValue(currentRow, ExcelColumn.DATA_TEST_REFERENCE.getValue()).trim();
    }

    private static Map<String, String> getDataTestList(Sheet testCaseSheet, Row currentRow, List<String> dataTestColumnNameList) {
        int numberOfColumn = testCaseSheet.getRow(ExcelColumn.FIELD_NAME.getValue()).getLastCellNum();
        Map<String, String> dataTestList = new HashMap<>();

        for (int currentJddColumn = ExcelColumn.DATA_TEST_REFERENCE.getValue() + 1; currentJddColumn < numberOfColumn; currentJddColumn++) {
            String columnNameValue = testCaseSheet.getRow(0).getCell(currentJddColumn).getStringCellValue();
            String dataTestValue = ExcelReader.getCellValue(currentRow, currentJddColumn);
            if(CollectionUtils.isNotEmpty(dataTestColumnNameList)){
                if(dataTestColumnNameList.contains(columnNameValue)){
                    dataTestList.put(columnNameValue, dataTestValue);
                }
            }else{
                dataTestList.put(columnNameValue, dataTestValue);
            }
        }
        return dataTestList;
    }

    private static Map<TargetKey, String> getTargetValueList(Row currentRow) {
        CommandName commandValue = getCommandValue(currentRow);
        String targetCellValue = ExcelReader.getCellValue(currentRow, ExcelColumn.TARGETS.getValue()).trim();
        return getTargetValueList(commandValue, targetCellValue);
    }

    private static Map<TargetKey, String> getTargetValueList(CommandName commandName, String targetCellValue) {
        Map<TargetKey, String> targets = new HashMap<>();
        if(StringUtils.isEmpty(targetCellValue)){
            return targets;
        }
        if (CommandName.CALL == commandName) {
            targets.put(TargetKey.CALL, targetCellValue);
        }else if (CollectionUtils.isNotEmpty(RegexUtil.match(XPATH_PATTERN, targetCellValue))) {
            targets.put(TargetKey.XPATH, targetCellValue);
        } else if (CollectionUtils.isNotEmpty(RegexUtil.match(JSON_PATTERN, targetCellValue))) {
            targets.put(TargetKey.COMBINAISON_OF_LOCATOR, targetCellValue);
        } else {
            targets.put(TargetKey.ID, targetCellValue);
        }
        return targets;
    }
}