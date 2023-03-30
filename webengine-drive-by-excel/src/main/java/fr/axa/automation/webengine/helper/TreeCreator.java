package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.constante.Constante;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseNodeDriveByExcel;
import fr.axa.automation.webengine.tree.TreeNode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class TreeCreator {

    public static List<TestCaseNodeDriveByExcel> createTree(List<TestCaseDataDriveByExcel> testCaseDataList) {
        return testCaseDataList.stream().map(TreeCreator::createTree).collect(Collectors.toList());
    }

    public static TestCaseNodeDriveByExcel createTree(TestCaseDataDriveByExcel testCaseData) {
        List<List<TreeNode>> nestedIfList = new Stack<>();
        List<TreeNode> optionalCommandList =  new Stack<>();
        TreeNode<CommandDataDriveByExcel> rootNode = new TreeNode<>(new CommandDataDriveByExcel());
        List<CommandDataDriveByExcel> commandDataSet = testCaseData.getCommandList();

        TreeNode treeNode;
        for (CommandDataDriveByExcel commandData : commandDataSet) {
            List<TreeNode> ifGroupList = nestedIfList.isEmpty() ? new Stack<>() : (List<TreeNode>) ((Stack)nestedIfList).peek();

            if((isNotOptional(commandData) || isOptional(commandData)) && CollectionUtils.isNotEmpty(optionalCommandList)){
                ((Stack)optionalCommandList).pop();
            }

            switch (CommandName.fromValue(commandData.getCommand())) {
                case IF:
                    treeNode = ifGroupList.isEmpty() ? rootNode : getLastElement((Stack) ifGroupList);
                    TreeNode ifNode = treeNode.addChild(commandData);
                    ((Stack)nestedIfList).push(getList(ifNode));
                    break;
                case ELSE_IF:
                case ELSE:
                    treeNode = ifGroupList.isEmpty() ? rootNode : getLastElement((Stack) ifGroupList);
                    TreeNode elseNode = treeNode.getParent().addChild(commandData);
                    ifGroupList.add(elseNode);
                    break;
                case END_IF:
                    treeNode = ifGroupList.isEmpty() ? rootNode : getLastElement((Stack) ifGroupList);
                    treeNode.getParent().addChild(commandData);
                    ((Stack)nestedIfList).pop();
                    break;
                default:
                    treeNode = ifGroupList.isEmpty() ? (optionalCommandList.isEmpty() ? rootNode : getLastElement((Stack) optionalCommandList)) : (optionalCommandList.isEmpty() ? getLastElement((Stack) ifGroupList) : getLastElement((Stack) optionalCommandList)) ;

                    if (isOptional(treeNode)){
                        if(isOptionalAndDependsOnPrevious(commandData)){
                            treeNode.addChild(commandData);
                        }
                    }else{
                        TreeNode optionalNode = treeNode.addChild(commandData);
                        if( isOptional(commandData)){
                            ((Stack)optionalCommandList).push(optionalNode);
                        }
                    }
                    break;
            }
        }
        rootNode.printTree("--",rootNode);
        return TestCaseNodeDriveByExcel.builder().uid(testCaseData.getUid()).name(testCaseData.getName()).treeNode(rootNode).build();
    }

    private static TreeNode getLastElement(Stack linkedParentList) {
        return (TreeNode) linkedParentList.peek();
    }

    private static List<TreeNode> getList(TreeNode treeNode) {
        List<TreeNode> parentNodeList = new Stack<>();
        ((Stack)parentNodeList).push(treeNode);
        return parentNodeList;
    }

    private static boolean isOptional(TreeNode treeNode){
        String optionalTreeNodeParent = ((CommandDataDriveByExcel)treeNode.getData()).getOptional();
        if(optionalTreeNodeParent != null && optionalTreeNodeParent.equalsIgnoreCase(Constante.OPTIONAL.getValue())){
            return true;
        }
        return false;
    }

    private static boolean isOptional(CommandDataDriveByExcel commandData){
        if(commandData!=null && commandData.getOptional().equalsIgnoreCase(Constante.OPTIONAL.getValue())){
            return true;
        }
        return false;
    }

    private static boolean isNotOptional(CommandDataDriveByExcel commandData){
        if(commandData!=null && commandData.getOptional().equalsIgnoreCase(StringUtils.EMPTY)){
            return true;
        }
        return false;
    }

    private static boolean isOptionalAndDependsOnPrevious(CommandDataDriveByExcel commandData){
        if(commandData!=null && commandData.getOptional().equalsIgnoreCase(Constante.OPTIONAL_AND_DEPENDS_ON_PREVIOUS.getValue())){
            return true;
        }
        return false;
    }

}