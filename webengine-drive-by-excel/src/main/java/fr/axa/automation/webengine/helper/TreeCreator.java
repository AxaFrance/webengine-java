package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.TestCaseDataNoCode;
import fr.axa.automation.webengine.object.TestCaseNodeNoCode;
import fr.axa.automation.webengine.tree.TreeNode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeCreator {

    public static List<TestCaseNodeNoCode> createTree(List<TestCaseDataNoCode> testCaseDataList) {
        return testCaseDataList.stream().map(TreeCreator::createTree).collect(Collectors.toList());
    }

    public static TestCaseNodeNoCode createTree(TestCaseDataNoCode testCaseData) {
        Deque<Deque<TreeNode>> nestedIfList = new LinkedList<>();
        Deque<TreeNode> optionalCommandList =  new LinkedList<>();
        TreeNode<CommandDataNoCode> rootNode = new TreeNode<>(new CommandDataNoCode());
        List<CommandDataNoCode> commandDataSet = testCaseData.getCommandList();

        TreeNode treeNode;
        for (CommandDataNoCode commandData : commandDataSet) {
            Deque<TreeNode> ifGroupList = nestedIfList.isEmpty() ? new LinkedList<>() : nestedIfList.getLast();

            if((commandData.isOptionalEmpty() || commandData.isOptional()) && CollectionUtils.isNotEmpty(optionalCommandList)){
                optionalCommandList.removeLast();
            }

            switch (commandData.getCommand()) {
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
        return TestCaseNodeNoCode.builder().uid(testCaseData.getUid()).name(testCaseData.getName()).treeNode(rootNode).build();
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
        CommandDataNoCode commandDataNoCode = ((CommandDataNoCode)treeNode.getData());
        return commandDataNoCode.isOptional();
    }
}