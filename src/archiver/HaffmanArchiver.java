package archiver;

import basics.Probability;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HaffmanArchiver extends AbstractArchiver {

    @Override
    public StringNode getTree(StringNode node) {

        List<StringNode> nodes = node.getChildren()
                .stream()
                .sorted(Comparator.comparing(a -> a.getProbability().getA()))
                .collect(Collectors.toList());

        while(nodes.size()!=1) {

            StringNode stringNode = new StringNode();

            StringNode left = nodes.get(0);
            StringNode right = nodes.get(1);

            stringNode.setLeftChild(left);
            stringNode.setRightChild(right);
            stringNode.setProbability(new Probability(null,left.getProbability().getA()+right.getProbability().getA(),
                    left.getProbability().getB()));

            nodes.remove(1);
            nodes.remove(0);
            nodes.add(stringNode);

            nodes = sort(nodes);
        }

        return nodes.get(0);

    }

    @Override
    public TreeNode getCodingTree(StringNode tree) {

        if (isNodeHasntGrandChildren(tree)) {
            TreeNode treeNode = new TreeNode();
            treeNode.setS(null);
            Map<Byte, TreeNode> node = new HashMap<>();

            StringNode leftChild = tree.getLeftChild();
            StringNode rightChild = tree.getRightChild();

            if (leftChild.getValue() != null && rightChild.getValue() != null) {
                node.put(ZERO, new TreeNode(leftChild.getValue(), null));
                node.put(ONE, new TreeNode(rightChild.getValue(), null));
            } else if (leftChild.getValue() != null) {
                node.put(ZERO, new TreeNode(leftChild.getValue(), null));
                node.put(ONE, getCodingTree(rightChild));
            } else if (rightChild != null) {
                node.put(ONE, new TreeNode(rightChild.getValue(), null));
                node.put(ZERO, getCodingTree(leftChild));
            }


            return treeNode;
        }

        TreeNode codingTreeLeft = getCodingTree(tree.getLeftChild());
        TreeNode codingTreeRight = getCodingTree(tree.getRightChild());

        TreeNode parent = new TreeNode();
        parent.setS(null);

        Map<Byte, TreeNode> map = new HashMap<>();

        map.put(ZERO, codingTreeLeft);
        map.put(ONE, codingTreeRight);

        parent.setNode(map);

        return parent;

    }

    @Override
    Map<String, byte[]> getCodingMap(StringNode tree) {
        if(isNodeHasntGrandChildren(tree)) {

            Map<String, byte[]> map = new HashMap<>();

            StringNode leftChild1 = tree.getLeftChild();
            StringNode rightChild1 = tree.getRightChild();

            if(leftChild1.getValue()!=null && rightChild1.getValue() == null) {
                map.put(leftChild1.getValue(),new byte[]{0});
            }else if (leftChild1.getValue()!=null && rightChild1.getValue() != null) {
                map.put(leftChild1.getValue(),new byte[]{0});
                map.put(rightChild1.getValue(),new byte[]{1});
            }

            if(leftChild1.getValue()==null) {
                map.putAll(getCodingMap(leftChild1));
            }
            if(rightChild1.getValue()==null) {
                map.putAll(getCodingMap(rightChild1));
            }

            return map;

        }

        Map<String, byte[]> decodingMapLeft = addCodeDigit(getCodingMap(tree.getLeftChild()), ZERO);

        Map<String, byte[]> decodingMapRight = addCodeDigit(getCodingMap(tree.getRightChild()), ONE);

        for(Map.Entry<String, byte[]> entry : decodingMapRight.entrySet()) {

            decodingMapLeft.put(entry.getKey(),entry.getValue());

        }

        return decodingMapLeft;
    }

    @Override
    public boolean isNodeHasntGrandChildren(StringNode node) {
        StringNode leftChild = node.getLeftChild();
        StringNode rightChild = node.getRightChild();

        if(leftChild.getValue()!=null) {
            return true;
        }

        if(rightChild.getValue()!=null) {
            return true;
        }
        return false;

    }



    //сортировка от меньшего к большему
    private static List<StringNode> sort(List<StringNode> nodes) {
        return nodes.stream().sorted(Comparator.comparing(a -> a.getProbability().getA())).collect(Collectors.toList());
    }
}
