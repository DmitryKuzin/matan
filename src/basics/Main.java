package basics;

import utils.BookReader;
import utils.ZipArchiver;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);

        String input = "";

        while(!input.equals("exit")) {
            System.out.println("Insert path to read from :");

            input = sc.next();

            if(input.equals("exit")) {
                return;
            }

            File file = new File(input);

            File zip = new File(file.getPath()+".zip");
            ZipArchiver.compress(file,zip);

            System.out.println("Uncompressed size: "+ file.length()+" bytes" + " | " + (double)file.length()/1024.0/1024.0 + " MB");
            System.out.println("Compressed size: "+ zip.length()+ " bytes"+ " | " + (double)zip.length()/1024.0/1024.0 + " MB");

            System.out.println("uncompressed/compressed: " + (double)file.length()/(double)zip.length());

            EntropySource entropySource = new EntropySource(BookReader.readBook(file));

            System.out.println("HU->" + Calc.entropy(entropySource,1));
            System.out.println("HU->" + Calc.entropy(entropySource,2));
        }

    }

//    public static Map<String, Probability> getHU(String src) {
//
//        Map<String,Double> alphabet = new HashMap<>();
//
//        char[] chars = src.toCharArray();
//
//        for (int i = 0; i < chars.length; i++) {
//            Character letter = chars[i];
//            String s = letter.toString();
//
//            if(!alphabet.keySet().contains(s)) {
//                alphabet.put(s,1D);
//            }else if (alphabet.keySet().contains(s)) {
//                Double oldValue = alphabet.get(s);
//                alphabet.replace(s, oldValue, oldValue+1);
//            }
//        }
//
//        Integer totalAplphabetLenght = alphabet.keySet().size();
//        Integer srcSize = src.length();
//
//
//        System.out.println("Total source size -> " + src.length() + " letters");
//        System.out.println("Total alphabet size -> " + totalAplphabetLenght + " letters");
//
//        int stringLen = 0;
//
//        System.out.println("alphabet :");
//
//        Object[] objects = alphabet.keySet().toArray();
//
//        for (int i = 0; i <objects.length; i++) {
//
//            System.out.print(objects[i].toString() + " ");
//            stringLen++;
//            if (stringLen > 9) {
//                stringLen = 0;
//                System.out.println("");
//            }
//        }
//
//        Map<String, Probability> matrix = new HashMap<>();
//
//        alphabet.keySet().forEach(key -> {
//            matrix.put(key, new Probability(key, alphabet.get(key), new Double(srcSize)));
//        });
//
//        return matrix;
//
//    }
//
//    public static void getHUAUA(String src, Map<String, Probability> probabilityMap, Integer lettersCount) {
//
//        Map<String, Integer> syllableMap = new HashMap<>();
//
//        char[] chars = src.toCharArray();
//
//        String previousLetter = "";
//
//        for (int i = 1; i < chars.length; i++) {
//
//            if(i==1) {
//                previousLetter = Character.toString(chars[0]);
//            }
//
//            String letter = Character.toString(chars[i]);
//
//            String syllable = previousLetter + letter;
//
//            if(!syllableMap.containsKey(syllable)) {
//                syllableMap.put(syllable, 1);
//            }else {
//                Integer syllableCount = syllableMap.get(syllable);
//                syllableMap.replace(syllable, syllableCount,syllableCount+1);
//            }
//
//            previousLetter = letter;
//
//        }
//
//        double uslovVer = 0d;
//
//        Double countOfSyllalbels = (double)lettersCount-1;
//
//        for(String sylalble : syllableMap.keySet()) {
//
//            Double countOfThisSyllable = syllableMap.get(sylalble).doubleValue();
//
//            String firstLetter = Character.toString(sylalble.charAt(0));
//
//            double firstLetterProbability = probabilityMap.get(firstLetter).getProbability();
//
//
//
//            double underLogExpression = countOfThisSyllable / countOfSyllalbels / firstLetterProbability;
//
//            double log = Math.log(underLogExpression)/Math.log(2.0);
//
//            uslovVer -= (countOfThisSyllable /countOfSyllalbels)*log;
//
//        }
//
//        System.out.print("H(u1|u2) -> ");
//        System.out.println(uslovVer);
//
//
//    }

}
