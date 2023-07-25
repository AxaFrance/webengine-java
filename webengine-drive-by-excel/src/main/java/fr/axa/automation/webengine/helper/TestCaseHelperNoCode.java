package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.constante.PredefinedDateTagValue;
import fr.axa.automation.webengine.constante.RegexContante;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.TestCaseDataNoCode;
import fr.axa.automation.webengine.object.TestCaseNodeNoCode;
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

public final class TestCaseHelperNoCode {

    public static AbstractTestCaseContext getTestCaseContext(AbstractTestCaseContext testCaseContext , String testCaseName, String dataTestColumnName){
        TestCaseNoCodeContext testCaseNoCodeContext = (TestCaseNoCodeContext) testCaseContext;
        List<TestCaseNodeNoCode> list = testCaseNoCodeContext.getTestSuiteData().getTestCaseNodeList().stream()
                                        .filter(testCaseNodeNoCode -> testCaseNodeNoCode.getName().equalsIgnoreCase(testCaseName))
                                        .collect(Collectors.toList());
        return TestCaseNoCodeContext.builder()
                .testSuiteData(testCaseNoCodeContext.getTestSuiteData())
                .testCaseToRun(list.get(0))
                .testCaseName(testCaseName)
                .dataTestColumnName(dataTestColumnName)
                .build();
    }

    public static String getReportTestCaseName(AbstractTestCaseContext testCaseContext){
        StringBuffer stringBuffer = new StringBuffer();
        TestCaseNoCodeContext testCaseNoCodeContext = (TestCaseNoCodeContext) testCaseContext;
        return stringBuffer.append(testCaseNoCodeContext.getTestCaseName()).append(" [ ").append("Data test column executed : ").append(testCaseNoCodeContext.getDataTestColumnName()).append(" ] ").toString();
    }

    public static List<String> getNameListByTestCase(TestCaseDataNoCode testCaseData){
        return testCaseData.getCommandList().stream().map(commandData -> commandData.getName()).filter(name->StringUtils.isNotEmpty(name)).collect(Collectors.toList());
    }

    public static List<String> getDataTestColumnName(TestCaseNodeNoCode testCaseNode){
        CommandDataNoCode commandDataNoCode = (CommandDataNoCode)testCaseNode.getTreeNode().getData();
        Map<String,String> dataTestList = commandDataNoCode.getDataTestMap();
        if(MapUtils.isNotEmpty(dataTestList)){
            return new ArrayList<>(commandDataNoCode.getDataTestMap().keySet());
        }else{
            if(testCaseNode.getTreeNode()!=null && CollectionUtils.isNotEmpty(testCaseNode.getTreeNode().getChildren())){
                TreeNode treeNode = (TreeNode) testCaseNode.getTreeNode().getChildren().get(0);
                if(treeNode!=null){
                    return new ArrayList<>(((CommandDataNoCode)treeNode.getData()).getDataTestMap().keySet());
                }
            }
        }
        throw new IllegalArgumentException("No data test column found");
    }

    public static List<String> getDataTestColumnName(TestCaseDataNoCode testCaseData){
        List<CommandDataNoCode>  commandDataList = testCaseData.getCommandList();
        List<String> dataTestColumn = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(commandDataList)){
            Optional<CommandDataNoCode> firstCommandData = commandDataList.stream().findFirst();
            if(firstCommandData.isPresent()){
                dataTestColumn = new ArrayList<>(firstCommandData.get().getDataTestMap().keySet());
            }
        }
        return dataTestColumn;
    }

    public static Map<String,List<String>> getReferencedValueByColumName(TestCaseDataNoCode testCaseData){
        Map<String,List<String>> dataTestByColunmName = new HashMap<>();
        List<String> dataTestColumnNameList = getDataTestColumnName(testCaseData);
        for (String dataTestColumnName :dataTestColumnNameList) {
            dataTestByColunmName.put(dataTestColumnName,getReferencedValueByColumName(testCaseData,dataTestColumnName));
        }
        return dataTestByColunmName;
    }

    public static List<String> getReferencedValueByColumName(TestCaseDataNoCode testCaseData, String dataTestNameColumn){
        List<String> filterDataTestList = new ArrayList<>();
        List<CommandDataNoCode>  commandDataList = testCaseData.getCommandList();
        List<String> dataTestByColumn = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(commandDataList)){
            dataTestByColumn = commandDataList.stream().map(commandData -> commandData.getDataTestMap().get(dataTestNameColumn)).collect(Collectors.toList());
        }

        dataTestByColumn.stream().forEach(value -> filterDataTestList.addAll(RegexUtil.match(RegexContante.INTERNAL_REGEX_VALUE,value)));
        return filterDataTestList.stream().filter(value -> !PredefinedDateTagValue.isContainsPredefinedDateTagValue(value)).collect(Collectors.toList());
    }
}
