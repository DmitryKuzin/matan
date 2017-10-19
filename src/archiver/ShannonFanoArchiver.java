package archiver;

import basics.EntropySource;
import basics.Probability;
import utils.BookReader;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShannonFanoArchiver {

    private static final byte ZERO = 0;

    private static final byte ONE = 1;

    public static void archive(File file) throws IOException {

        String book = BookReader.readBook(file);

        EntropySource entropySource = new EntropySource(book);

        StringNode root = new StringNode();

        for(Map.Entry<String,Probability> entry : entropySource.getProbabilityMatrix().entrySet()) {
            StringNode stringNode = new StringNode();
            stringNode.setValue(entry.getKey());
            stringNode.setProbability(entry.getValue());

            root.getChildren().add(stringNode);
        }

        StringNode computedTree = getTree(root);

        Map<String, byte[]> codingMap = getCodingMap(computedTree);

        codingMap.forEach((key, value) -> System.out.println(key + "->" + Arrays.toString(revertArray(value))));

        FileOutputStream fileOutputStream = new FileOutputStream(new File(file.getPath()+ ".zip"));

        char[] arr = book.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            String s = Character.toString(arr[i]);
            byte[] bytes = codingMap.get(s);

            fileOutputStream.write(bytes);
        }

        fileOutputStream.close();



    }

    private static StringNode getTree(StringNode parent) {
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

    private static Map<String,byte[]> getCodingMap(StringNode tree) {

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

    private static Map<String, byte[]> addCodeDigit(Map<String, byte[]> map, byte digit) {

        for(Map.Entry<String, byte[]> entry : map.entrySet()) {

            byte[] bytes = Arrays.copyOf(entry.getValue(), entry.getValue().length + 1);

            bytes[entry.getValue().length] = digit;

            map.replace(entry.getKey(), bytes);
        }

        return map;
    }

    private static byte[] revertArray(byte[] array) {

        byte[] reverted = new byte[array.length];

        int j = 0;

        for (int i = array.length-1; i >= 0 ; i--) {

            reverted[j] = array[i];

            j++;

        }

        return reverted;
    }

    //TODO: реализовать бинарное дерево и запихнуть в него декодирующее дерево

}
