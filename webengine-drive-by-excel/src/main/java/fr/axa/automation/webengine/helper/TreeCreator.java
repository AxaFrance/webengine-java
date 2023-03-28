package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.tree.TreeNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class TreeCreator {

    public static TreeNode<CommandDataDriveByExcel> createTree(TestCaseDataDriveByExcel testCaseData) {
        Stack<List<TreeNode>> parentStack = new Stack<>();
        TreeNode<CommandDataDriveByExcel> rootNode = new TreeNode<>(new CommandDataDriveByExcel());
        List<CommandDataDriveByExcel> commandDataSet = testCaseData.getCommandList();

        TreeNode treeNode;
        for (CommandDataDriveByExcel commandData : commandDataSet) {
            List<TreeNode> linkedParentList = parentStack.isEmpty() ? new LinkedList<>() : parentStack.peek();

            switch (CommandName.fromValue(commandData.getCommand())) {
                case IF:
                    treeNode = linkedParentList.isEmpty() ? rootNode : linkedParentList.get(linkedParentList.size() - 1);
                    TreeNode ifNode = treeNode.addChild(commandData);
                    parentStack.push(addCurrentParentNode(ifNode));
                    break;
                case ELSE_IF:
                case ELSE:
                    treeNode = linkedParentList.isEmpty() ? rootNode : linkedParentList.get(linkedParentList.size() - 1);
                    TreeNode elseNode = treeNode.getParent().addChild(commandData);
                    linkedParentList.add(elseNode);
                    break;
                case END_IF:
                    treeNode = linkedParentList.isEmpty() ? rootNode : linkedParentList.get(linkedParentList.size() - 1);
                    treeNode.getParent().addChild(commandData);
                    parentStack.pop();
                    break;
                default:
                    treeNode = linkedParentList.isEmpty() ? rootNode : linkedParentList.get(linkedParentList.size() - 1);

                    if(((CommandDataDriveByExcel)treeNode.getData()).getOptional()!=null && ((CommandDataDriveByExcel)treeNode.getData()).getOptional().equalsIgnoreCase("optional")){
                        if(commandData.getOptional().equalsIgnoreCase("optional and depends on previous")){
                            treeNode.addChild(commandData);
                        }

                        if(commandData.getOptional().equalsIgnoreCase("") || commandData.getOptional().equalsIgnoreCase("optional")){
                            parentStack.pop();
                        }
                    }else{
                        treeNode.addChild(commandData);
                    }
                    if(commandData.getOptional().equalsIgnoreCase("optional")){
                        TreeNode optionalNode = treeNode.addChild(commandData);
                        parentStack.push(addCurrentParentNode(optionalNode));
                    }
                    break;
            }
        }
        return rootNode;
    }



    private static List<TreeNode> addCurrentParentNode(TreeNode treeNode) {
        List<TreeNode> parentNodeList = new LinkedList<>();
        parentNodeList.add(treeNode);
        return parentNodeList;
    }

}