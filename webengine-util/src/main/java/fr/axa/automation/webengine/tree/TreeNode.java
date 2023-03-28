package fr.axa.automation.webengine.tree;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.LinkedList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class TreeNode<T>  {

    T data;
    TreeNode<T> parent;
    List<TreeNode<T>> children;

    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<>();
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    public void addChildren(List<TreeNode<T>> children) {
        children.forEach(each -> each.setParent(this));
        this.children.addAll(children);
    }

    public void deleteNode() {
        if (parent != null) {
            int index = this.parent.getChildren().indexOf(this);
            this.parent.getChildren().remove(this);
            for (TreeNode<T> each : getChildren()) {
                each.setParent(this.parent);
            }
            this.parent.getChildren().addAll(index, this.getChildren());
        } else {
            deleteRootNode();
        }
        this.getChildren().clear();
    }

    public TreeNode<T> deleteRootNode() {
        if (parent != null) {
            throw new IllegalStateException("deleteRootNode not called on root");
        }
        TreeNode<T> newParent = null;
        if (!getChildren().isEmpty()) {
            newParent = getChildren().get(0);
            newParent.setParent(null);
            getChildren().remove(0);
            for (TreeNode<T> each : getChildren()) {
                each.setParent(newParent);
            }
            newParent.getChildren().addAll(getChildren());
        }
        this.getChildren().clear();
        return newParent;
    }

    public void printTree(String prefixe,TreeNode node) {
        System.out.println(prefixe+node.getData());
        List<TreeNode> childList = node.getChildren();
        for (TreeNode child : childList) {
            printTree(prefixe+prefixe,child);
        }
    }
}