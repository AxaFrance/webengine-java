package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.constante.PredefinedDateTagValue;
import fr.axa.automation.webengine.constante.RegexContante;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseNodeDriveByExcel;
import fr.axa.automation.webengine.tree.TreeNode;
import fr.axa.automation.webengine.util.RegexUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class TestCaseHelperDriveByExcel {

    public static AbstractTestCaseContext getTestCaseContext(AbstractTestCaseContext testCaseContext , String testCaseName){
        TestCaseDriveByExcelContext testCaseDriveByExcelContext = (TestCaseDriveByExcelContext) testCaseContext;
        List<TestCaseNodeDriveByExcel> list = testCaseDriveByExcelContext.getTestSuiteData().getTestCaseNodeList().stream().filter(testCaseNodeDriveByExcel -> testCaseNodeDriveByExcel.getName().equalsIgnoreCase(testCaseName)).collect(Collectors.toList());
        return TestCaseDriveByExcelContext.builder().testCaseName(testCaseName).webDriver(testCaseDriveByExcelContext.getWebDriver()).testSuiteData(testCaseDriveByExcelContext.getTestSuiteData()).testCaseToRun(list.get(0)).dataTestColumnName(testCaseDriveByExcelContext.getDataTestColumnName()).build();
    }

    public static String getReportTestCaseName(AbstractTestCaseContext testCaseContext){
        StringBuffer stringBuffer = new StringBuffer();
        TestCaseDriveByExcelContext testCaseDriveByExcelContext = (TestCaseDriveByExcelContext) testCaseContext;
        return stringBuffer.append(testCaseDriveByExcelContext.getTestCaseName()).append(" [ ").append("Data test column executed : ").append(testCaseDriveByExcelContext.getDataTestColumnName()).append(" ] ").toString();
    }

    public static List<String> getNameListByTestCase(TestCaseDataDriveByExcel testCaseData){
        return testCaseData.getCommandList().stream().map(commandData -> commandData.getName()).filter(name->StringUtils.isNotEmpty(name)).collect(Collectors.toList());
    }

    public static List<String> getDataTestColumnName(TestCaseNodeDriveByExcel testCaseNode){
        CommandDataDriveByExcel commandDataDriveByExcel = (CommandDataDriveByExcel)testCaseNode.getTreeNode().getData();
        Map<String,String> dataTestList = commandDataDriveByExcel.getDataTestMap();
        if(MapUtils.isNotEmpty(dataTestList)){
            return new ArrayList<>(commandDataDriveByExcel.getDataTestMap().keySet());
        }else{
            if(testCaseNode.getTreeNode()!=null && CollectionUtils.isNotEmpty(testCaseNode.getTreeNode().getChildren())){
                TreeNode treeNode = (TreeNode) testCaseNode.getTreeNode().getChildren().get(0);
                if(treeNode!=null){
                    return new ArrayList<>(((CommandDataDriveByExcel)treeNode.getData()).getDataTestMap().keySet());
                }
            }
        }
        throw new IllegalArgumentException("No data test column found");
    }

    public static List<String> getDataTestColumnName(TestCaseDataDriveByExcel testCaseData){
        List<CommandDataDriveByExcel>  commandDataList = testCaseData.getCommandList();
        List<String> dataTestColumn = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(commandDataList)){
            Optional<CommandDataDriveByExcel> firstCommandData = commandDataList.stream().findFirst();
            if(firstCommandData.isPresent()){
                dataTestColumn = new ArrayList<>(firstCommandData.get().getDataTestMap().keySet());
            }
        }
        return dataTestColumn;
    }

    public static Map<String,List<String>> getReferencedValueByColumName(TestCaseDataDriveByExcel testCaseData){
        Map<String,List<String>> dataTestByColunmName = new HashMap<>();
        List<String> dataTestColumnNameList = getDataTestColumnName(testCaseData);
        for (String dataTestColumnName :dataTestColumnNameList) {
            dataTestByColunmName.put(dataTestColumnName,getReferencedValueByColumName(testCaseData,dataTestColumnName));
        }
        return dataTestByColunmName;
    }

    public static List<String> getReferencedValueByColumName(TestCaseDataDriveByExcel testCaseData, String dataTestNameColumn){
        List<String> filterDataTestList = new ArrayList<>();
        List<CommandDataDriveByExcel>  commandDataList = testCaseData.getCommandList();
        List<String> dataTestByColumn = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(commandDataList)){
            dataTestByColumn = commandDataList.stream().map(commandData -> commandData.getDataTestMap().get(dataTestNameColumn)).collect(Collectors.toList());
        }

        dataTestByColumn.stream().forEach(value -> filterDataTestList.addAll(RegexUtil.match(RegexContante.REFERENCED_VALUE_REGEX,value)));
        return filterDataTestList.stream().filter(value -> !PredefinedDateTagValue.isContainsPredefinedDateTagValue(value)).collect(Collectors.toList());
    }
}
