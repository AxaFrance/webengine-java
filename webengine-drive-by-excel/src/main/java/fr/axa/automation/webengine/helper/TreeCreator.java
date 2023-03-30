package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseNodeDriveByExcel;
import fr.axa.automation.webengine.tree.TreeNode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeCreator {

    public static List<TestCaseNodeDriveByExcel> createTree(List<TestCaseDataDriveByExcel> testCaseDataList) {
        return testCaseDataList.stream().map(TreeCreator::createTree).collect(Collectors.toList());
    }

    public static TestCaseNodeDriveByExcel createTree(TestCaseDataDriveByExcel testCaseData) {
        Deque<Deque<TreeNode>> nestedIfList = new LinkedList<>();
        Deque<TreeNode> optionalCommandList =  new LinkedList<>();
        TreeNode<CommandDataDriveByExcel> rootNode = new TreeNode<>(new CommandDataDriveByExcel());
        List<CommandDataDriveByExcel> commandDataSet = testCaseData.getCommandList();

        TreeNode treeNode;
        for (CommandDataDriveByExcel commandData : commandDataSet) {
            Deque<TreeNode> ifGroupList = nestedIfList.isEmpty() ? new LinkedList<>() : nestedIfList.getLast();

            if((commandData.isOptionalEmpty() || commandData.isOptional()) && CollectionUtils.isNotEmpty(optionalCommandList)){
                optionalCommandList.removeLast();
            }

            switch (CommandName.fromValue(commandData.getCommand())) {
                case IF:
                    treeNode = ifGroupList.isEmpty() ? rootNode : getLastElement(ifGroupList);
                    TreeNode ifNode = treeNode.addChild(commandData);
                    nestedIfList.addLast(getList(ifNode));
                    break;
                case ELSE_IF:
                case ELSE:
                    treeNode = ifGroupList.isEmpty() ? rootNode : getLastElement(ifGroupList);
                    TreeNode elseNode = treeNode.getParent().addChild(commandData);
                    ifGroupList.addLast(elseNode);
                    break;
                case END_IF:
                    treeNode = ifGroupList.isEmpty() ? rootNode : getLastElement(ifGroupList);
                    treeNode.getParent().addChild(commandData);
                    nestedIfList.removeLast();
                    break;
                default:
                    treeNode = ifGroupList.isEmpty() ? (optionalCommandList.isEmpty() ? rootNode : getLastElement(optionalCommandList)) : (optionalCommandList.isEmpty() ? getLastElement(ifGroupList) : getLastElement(optionalCommandList)) ;

                    if (isOptional(treeNode)){
                        if(commandData.isOptionalAndDependsOnPrevious()){
                            treeNode.addChild(commandData);
                        }
                    }else{
                        TreeNode optionalNode = treeNode.addChild(commandData);
                        if(commandData.isOptional()){
                            optionalCommandList.addLast(optionalNode);
                        }
                    }
                    break;
            }
        }
        rootNode.printTree("--",rootNode);
        return TestCaseNodeDriveByExcel.builder().uid(testCaseData.getUid()).name(testCaseData.getName()).treeNode(rootNode).build();
    }

    private static TreeNode getLastElement(Deque linkedParentList) {
        return (TreeNode)linkedParentList.getLast();
    }

    private static Deque<TreeNode> getList(TreeNode treeNode) {
        Deque<TreeNode> parentNodeList = new LinkedList<>();
        parentNodeList.add(treeNode);
        return parentNodeList;
    }

    private static boolean isOptional(TreeNode treeNode){
        CommandDataDriveByExcel commandDataDriveByExcel = ((CommandDataDriveByExcel)treeNode.getData());
        return commandDataDriveByExcel.isOptional();
    }
}