package archiver;

import basics.Calc;
import basics.EntropySource;
import basics.Probability;
import org.codehaus.jackson.map.ObjectMapper;
import utils.Array;
import utils.BitOutputStream;
import utils.BookReader;
import utils.ByteUtils;

import java.io.*;
import java.util.*;

public abstract class AbstractArchiver {

    final byte ZERO = 0;

    final byte ONE = 1;

    public void archive(File file, boolean twoSymbMode) throws IOException {

        String book = BookReader.readBook(file);

        EntropySource entropySource = new EntropySource(book);

        if(twoSymbMode) {
            System.out.println("2 symbol entropy " + Calc.entropy(entropySource,2));
        }else {
            System.out.println("1 symbol entropy " + Calc.entropy(entropySource,1));
        }

        StringNode root = new StringNode();

        Double syllableCount = (double)entropySource.getSyllableMap().size();

        if(twoSymbMode) {
            for(Map.Entry<String,Integer> entry : entropySource.getSyllableMap().entrySet()) {
                StringNode stringNode = new StringNode();
                stringNode.setValue(entry.getKey());
                Probability probability = new Probability(entry.getKey(),(double)entry.getValue(),syllableCount);

                stringNode.setProbability(probability);

                root.getChildren().add(stringNode);
            }
        }else {

            for(Map.Entry<String,Probability> entry : entropySource.getProbabilityMatrix().entrySet()) {
                StringNode stringNode = new StringNode();
                stringNode.setValue(entry.getKey());
                stringNode.setProbability(entry.getValue());

                root.getChildren().add(stringNode);
            }
        }

        StringNode computedTree = getTree(root);

        Map<String, byte[]> codingMap = getCodingMap(computedTree);

        TreeNode decodingTree = getCodingTree(computedTree);

        byte[] code = code(book, codingMap, decodingTree, twoSymbMode);



        System.out.println("bits/symbol:"+ (double)code.length/(double)book.length());

//        codingMap.forEach((key, value) -> System.out.println(key + "->" + Arrays.toString(revertArray(value))));

        String ext = twoSymbMode? "2symb" : "1symb";

        String filePath = file.getPath();

        String fileName = filePath.substring(0, filePath.indexOf("."));

        BitOutputStream bos = new BitOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(
                                new File(fileName + ext +".bin"))));

        boolean[] convert = convert(code);

        for (boolean b : convert) {
            bos.write(b);
        }
        bos.close();


    }

    private boolean[] convert(byte[] array) {

        boolean[] arr = new boolean[array.length];


        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i] != ZERO;
        }

        return arr;

    }


    public static String deArchive(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);

        byte[] firstBlock = new byte[8];
        fileInputStream.read(firstBlock);

        Long mapSize = ByteUtils.bytesToLong(firstBlock);

        byte[] decodingBlock = new byte[mapSize.intValue()];

        fileInputStream.read(decodingBlock);

        String s = new String(decodingBlock);

        ObjectMapper objectMapper = new ObjectMapper();

        TreeNode treeNode = objectMapper.readValue(s, TreeNode.class);

        byte[] buffer = new byte[10000];

        int read = fileInputStream.read(buffer);

        StringBuilder result = new StringBuilder();

        while(read>-1) {

            DecodingEntity decode = decode(buffer, treeNode.getNode());

            result.append(decode.getS());

            byte[] arr = new byte[10000-decode.getNonDecodedBytes().length];

            byte[] nonDecoded = new byte[decode.getNonDecodedBytes().length];

            for (int i = 0; i < decode.getNonDecodedBytes().length; i++) {
                nonDecoded[i] = decode.getNonDecodedBytes()[i];
            }

            buffer = Array.copyAllInto(arr,nonDecoded);

            read = fileInputStream.read(buffer);

        }

        return result.toString();

    }

    abstract StringNode getTree(StringNode parent);

    public TreeNode getCodingTree(StringNode tree) {

        if (isNodeHasntGrandChildren(tree)) {
            TreeNode treeNode = new TreeNode();
            treeNode.setS(null);
            Map<Byte, TreeNode> node = new HashMap<>();

            StringNode leftChild = tree.getLeftChild();
            StringNode rightChild = tree.getRightChild();

            if (leftChild != null && rightChild != null) {
                node.put(ZERO, new TreeNode(leftChild.getValue(), null));
                node.put(ONE, new TreeNode(rightChild.getValue(), null));
            } else if (leftChild != null) {
                node.put(ZERO, new TreeNode(leftChild.getValue(), null));
            } else if (rightChild != null) {
                node.put(ZERO, new TreeNode(rightChild.getValue(), null));
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

    abstract Map<String,byte[]> getCodingMap(StringNode tree);

    private static byte[] revertArray(byte[] array) {

        byte[] reverted = new byte[array.length];

        int j = 0;

        for (int i = array.length-1; i >= 0 ; i--) {

            reverted[j] = array[i];

            j++;

        }

        return reverted;
    }

    private static byte[] code(String src, Map<String, byte[]> codingMap, TreeNode decodingTree, boolean twoSymbMode) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        byte[] bytes = objectMapper.writeValueAsBytes(decodingTree);

        long size = (long)bytes.length;

        byte[] firstBlock = ByteUtils.longToBytes(size);

        byte[] result = Array.copyAllInto(firstBlock, bytes);

        char[] arr = src.toCharArray();

        if(twoSymbMode) {
            for (int i = 0; i < arr.length; i+=2) {
                String s = Character.toString(arr[i]);
                s+=Character.toString(arr[i+1]);
                byte[] bytes1 = codingMap.get(s);
                if(bytes1==null) {
                   bytes1 = new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0};
                }
                result = Array.copyAllInto( result, bytes1);
            }

            return result;
        }else {
            for (int i = 0; i < arr.length; i++) {
                String s = Character.toString(arr[i]);
                byte[] bytes1 = codingMap.get(s);
                if(bytes1==null) {
                    bytes1 = new byte[]{0,0,0,0,0};
                }
                result = Array.copyAllInto( result, bytes1);
            }

            return result;
        }

    }

    private static DecodingEntity decode(byte[] src, Map<Byte,TreeNode> decodingTree) {

        StringBuilder stringBuilder = new StringBuilder();

        List<Byte> nonDecodedBytes = new ArrayList<>();

        List<Byte> bytesBuff = new ArrayList<>();

        Map<Byte,TreeNode> tmp = decodingTree;

        for (int i = 0; i < src.length; i++) {
            TreeNode node = tmp.get(src[i]);
            if(node != null) {
                if(node.getS() != null) {
                    stringBuilder.append(node.getS());

                    tmp = decodingTree;
                    bytesBuff = new ArrayList<>();
                }else if(node.getNode().keySet().size()>0) {
                    bytesBuff.add(src[i]);
                    tmp = node.getNode();
                }else {
                    tmp = decodingTree;
                    nonDecodedBytes.addAll(bytesBuff);
                    bytesBuff = new ArrayList<>();
                }
            }else {
                System.out.println("I would never be here!");
                nonDecodedBytes.add(src[i]);
            }
        }

        Byte[] ndb = new Byte[nonDecodedBytes.size()];

        return new DecodingEntity(stringBuilder.toString(), nonDecodedBytes.toArray(ndb));

    }

    abstract boolean isNodeHasntGrandChildren(StringNode node);

    public Map<String, byte[]> addCodeDigit(Map<String, byte[]> map, byte digit) {

        for(Map.Entry<String, byte[]> entry : map.entrySet()) {

            byte[] bytes = Arrays.copyOf(entry.getValue(), entry.getValue().length + 1);

            bytes[entry.getValue().length] = digit;

            map.replace(entry.getKey(), bytes);
        }

        return map;
    }
}
