package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.constante.TargetKey;
import fr.axa.automation.webengine.global.ExcelColumn;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ExcelConverter {

    public static final String XPATH_PATTERN = "^([/]{1,2}.*)$";
    public static final String MANY_LOCATED_PATTERN = "^[\\{].*[\\n|\\r]";

    public static TestSuiteDataDriveByExcel convert(String excelFileName, Map<String, List<String>> testCaseAndDataTestColumNameMap) {
        List<TestCaseDataDriveByExcel> testCaseList = new LinkedList<>();
        Workbook workbook = ExcelReader.getWorkbook(excelFileName);
        List<String> sheetNameList = ExcelReader.getAllSheetName(workbook);
        assertSheetName(sheetNameList,testCaseAndDataTestColumNameMap);
        if (MapUtils.isEmpty(testCaseAndDataTestColumNameMap)) {
            testCaseList.addAll(getTestCaseDataList(workbook, getTestCaseNameAndDataTestColumnName(sheetNameList)));
        } else {
            testCaseList.addAll(getTestCaseDataList(workbook, testCaseAndDataTestColumNameMap));
        }

        TestSuiteDataDriveByExcel testSuite = TestSuiteDataDriveByExcel.builder()
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
            throw new IllegalArgumentException("These sheets doesn't exist :"+ sheetNameDoesntExist);
        }
    }

    private static Map<String, List<String>> getTestCaseNameAndDataTestColumnName(List<String> sheetNameList){
        return sheetNameList.stream().distinct().collect(Collectors.toMap(Function.identity(), Arrays::asList));
    }

    private static boolean isEndOfFile(Sheet currentSheet, Integer rowIndex) {
        String cellValue = ExcelReader.getCellValue(currentSheet, rowIndex, ExcelColumn.COMMAND.getValue());
        if(CommandName.fromValue(cellValue) == CommandName.END_SCENARIO){
            return true;
        }
        return false;
    }

    private static Set<TestCaseDataDriveByExcel> getTestCaseDataList(Workbook workbook, Map<String, List<String>> testCaseAndDataTestColumNameMap ) {
        Map<String, TestCaseDataDriveByExcel> testCaseDataMap = new HashMap<>();
        if (MapUtils.isNotEmpty(testCaseAndDataTestColumNameMap)) {
            for (String testCaseToRun : testCaseAndDataTestColumNameMap.keySet()) {
                if(!testCaseDataMap.containsKey(testCaseToRun)){
                    testCaseDataMap.putAll(getTestCaseDataList(workbook, testCaseToRun, testCaseAndDataTestColumNameMap.get(testCaseToRun)));
                }
            }
        }
        return new HashSet<>(testCaseDataMap.values());
    }

    private static Map<String, TestCaseDataDriveByExcel> getTestCaseDataList(Workbook workbook, String testCaseSheetName, List<String> dataTestColumnNameList) {

        Map<String, TestCaseDataDriveByExcel> testCaseDataMap = new HashMap<>();
        List<CommandDataDriveByExcel> commandDataList = new LinkedList<>();
        Sheet testCaseSheet = workbook.getSheet(testCaseSheetName);
        int rowIndex = 1;

        while (!isEndOfFile(testCaseSheet, rowIndex)) {
            Row currentRow = testCaseSheet.getRow(rowIndex);
            CommandDataDriveByExcel commandData = CommandDataDriveByExcel.builder()
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
                Map<String, TestCaseDataDriveByExcel> testCaseDataCalledMap = getTestCaseDataList(workbook, testCaseNameToCall, dataTestColumnNameList);
                if (!testCaseDataMap.containsKey(testCaseNameToCall)) {
                    testCaseDataMap.putAll(testCaseDataCalledMap);
                }
            }
            rowIndex++;
        }

        testCaseDataMap.put(testCaseSheetName, getTestCaseData(testCaseSheet, commandDataList));
        return testCaseDataMap;
    }

    private static TestCaseDataDriveByExcel getTestCaseData(Sheet testCaseSheet, List<CommandDataDriveByExcel> commandDataList) {
        TestCaseDataDriveByExcel testCaseData = TestCaseDataDriveByExcel.builder()
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
        }else if (CommandName.OPEN == commandName) {
            targets.put(TargetKey.OPEN, targetCellValue);
        }else if (CollectionUtils.isNotEmpty(RegexUtil.match(XPATH_PATTERN, targetCellValue))) {
            targets.put(TargetKey.XPATH, targetCellValue);
        } else if (CollectionUtils.isNotEmpty(RegexUtil.match(MANY_LOCATED_PATTERN, targetCellValue))) {
            targets.put(TargetKey.COMBINAISON_OF_LOCATOR, targetCellValue);
        } else {
            targets.put(TargetKey.ID, targetCellValue);
        }
        return targets;
    }
}