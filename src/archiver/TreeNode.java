package archiver;

import java.io.Serializable;
import java.util.Map;

public class TreeNode implements Serializable {

    private String s;
    private Map<Byte,TreeNode> node;

    public TreeNode() {
    }

    public TreeNode(String s, Map<Byte, TreeNode> node) {
        this.s = s;
        this.node = node;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public Map<Byte, TreeNode> getNode() {
        return node;
    }

    public void setNode(Map<Byte, TreeNode> node) {
        this.node = node;
    }
}
