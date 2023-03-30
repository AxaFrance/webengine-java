package fr.axa.automation.webengine.object;

import fr.axa.automation.webengine.tree.TreeNode;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TestCaseNodeDriveByExcel {
    private String uid;
    private String name;
    private TreeNode treeNode;
}