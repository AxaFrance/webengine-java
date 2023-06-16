package fr.axa.automation.webengine.object;

import fr.axa.automation.webengine.tree.TreeNode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Builder
public class TestCaseNodeNoCode {
    String uid;
    String name;
    TreeNode treeNode;
}