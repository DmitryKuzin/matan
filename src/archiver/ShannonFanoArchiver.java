package archiver;

import basics.Probability;
import java.util.*;
import java.util.stream.Collectors;

public class ShannonFanoArchiver extends AbstractArchiver{

    /**
     *
     * @return coding tree built by Shanon-Fano's algorithm
     */

    public StringNode getTree(StringNode parent) {
        if(parent.getChildren().size() <= 1) {
            return parent;
        }

        StringNode aChild = new StringNode();

        StringNode bChild = new StringNode();


        Double half = parent.getChildren().stream().mapToDouble(child-> child.getProbability().getA()).sum() / 2.0;

        Double aSum = 0.0;

        for(StringNode entry : parent.getChildren()) {
            if (entry.getProbability().getA() + aSum <= half) {
                aSum += entry.getProbability().getA();
                aChild.getChildren().add(entry);
            } else {
                bChild.getChildren().add(entry);
            }
        }

        StringNode aTree = getTree(aChild);
        StringNode bTree = getTree(bChild);

        if(aTree!= null) {
            parent.setLeftChild(aTree);
        }

        if(bTree!=null) {
            parent.setRightChild(bTree);
        }

        return parent;

    }

    @Override
    boolean isNodeHasntGrandChildren(StringNode node) {
        return node.getChildren().size()<3 && node.getChildren().size()>0;
    }

    @Override
    public Map<String,byte[]> getCodingMap(StringNode tree) {

        if(tree.getChildren().size()<3 && tree.getChildren().size()>0) {

            Map<String, byte[]> map = new HashMap<>();

            if(tree.getChildren().size() == 1) {

                StringNode stringNode = tree.getChildren().get(0);

                map.put(stringNode.getValue(),new byte[]{0});

            }else {

                StringNode leftChild = tree.getLeftChild().getChildren().get(0);

                map.put(leftChild.getValue(),new byte[]{0});

                StringNode rightChild = tree.getRightChild().getChildren().get(0);

                map.put(rightChild.getValue(),new byte[]{1});

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

}
