package archiver;

import basics.Probability;

import java.util.ArrayList;
import java.util.List;

public class StringNode {

    private String value;
    private byte[] code;
    private List<StringNode> children;
    private Probability probability;
    private StringNode leftChild;
    private StringNode rightChild;

    public StringNode() {
        this.children = new ArrayList<>();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<StringNode> getChildren() {
        return children;
    }

    public void setChildren(List<StringNode> children) {
        this.children = children;
    }

    public Probability getProbability() {
        return probability;
    }

    public void setProbability(Probability probability) {
        this.probability = probability;
    }

    public StringNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(StringNode leftChild) {
        this.leftChild = leftChild;
    }

    public StringNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(StringNode rightChild) {
        this.rightChild = rightChild;
    }

    public byte[] getCode() {
        return code;
    }

    public void setCode(byte[] code) {
        this.code = code;
    }
}
