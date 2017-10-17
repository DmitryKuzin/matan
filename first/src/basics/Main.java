package basics;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
            zipArchive(file,zip);

            System.out.println("Uncompressed size: "+ file.length()+" bytes" + " | " + (double)file.length()/1024.0/1024.0 + " MB");
            System.out.println("Compressed size: "+ zip.length()+ " bytes"+ " | " + (double)zip.length()/1024.0/1024.0 + " MB");

            System.out.println("uncompressed/compressed: " + (double)file.length()/(double)zip.length());

            String book = BookReader.readBook(file);

            System.out.println("bits per symbol: " + (double)zip.length()*8.0/ book.length());

            Map<String, Probability> probabilityMap = getHU(book);

            getHUAUA(book,probabilityMap, book.length());
        }

    }

    public static Map<String, Probability> getHU(String src) {

        Map<String,Double> alphabet = new HashMap<>();

        char[] chars = src.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            Character letter = chars[i];
            String s = letter.toString();

            if(!alphabet.keySet().contains(s)) {
                alphabet.put(s,1D);
            }else if (alphabet.keySet().contains(s)) {
                Double oldValue = alphabet.get(s);
                alphabet.replace(s, oldValue, oldValue+1);
            }
        }

        Integer totalAplphabetLenght = alphabet.keySet().size();
        Integer srcSize = src.length();


        System.out.println("Total source size -> " + src.length() + " letters");
        System.out.println("Total alphabet size -> " + totalAplphabetLenght + " letters");

        int stringLen = 0;

        System.out.println("alphabet :");

        Object[] objects = alphabet.keySet().toArray();

        for (int i = 0; i <objects.length; i++) {

            System.out.print(objects[i].toString() + " ");
            stringLen++;
            if (stringLen > 9) {
                stringLen = 0;
                System.out.println("");
            }
        }

        System.out.println("\n");

        Map<String, Probability> matrix = new HashMap<>();

        alphabet.keySet().forEach(key -> {
            matrix.put(key, new Probability(key, alphabet.get(key), new Double(srcSize)));
        });

        System.out.print("H(U)->");
        System.out.println(calculateEntropy(matrix) + " bits/symbol");

        return matrix;

    }

    public static void getHUAUA(String src, Map<String, Probability> probabilityMap, Integer lettersCount) {

        Map<String, Integer> syllableMap = new HashMap<>();

        char[] chars = src.toCharArray();

        String previousLetter = "";

        for (int i = 1; i < chars.length; i++) {

            if(i==1) {
                previousLetter = Character.toString(chars[0]);
            }

            String letter = Character.toString(chars[i]);

            String syllable = previousLetter + letter;

            if(!syllableMap.containsKey(syllable)) {
                syllableMap.put(syllable, 1);
            }else {
                Integer syllableCount = syllableMap.get(syllable);
                syllableMap.replace(syllable, syllableCount,syllableCount+1);
            }

            previousLetter = letter;

        }

        double uslovVer = 0d;

        Double countOfSyllalbels = (double)lettersCount-1;

        for(String sylalble : syllableMap.keySet()) {

            Double countOfThisSyllable = syllableMap.get(sylalble).doubleValue();

            String firstLetter = Character.toString(sylalble.charAt(0));

            double firstLetterProbability = probabilityMap.get(firstLetter).getProbability();



            double underLogExpression = countOfThisSyllable / countOfSyllalbels / firstLetterProbability;

            double log = Math.log(underLogExpression)/Math.log(2.0);

            uslovVer -= (countOfThisSyllable /countOfSyllalbels)*log;

        }

        System.out.print("H(u1|u2) -> ");
        System.out.println(uslovVer);


    }

    private static Double calculateEntropy(Map<String, Probability> probabilityMap) {
        Double result = 0D;

        for (String s : probabilityMap.keySet()) {

            double px = probabilityMap.get(s).getProbability();
            double logPx = Math.log(probabilityMap.get(s).getProbability())/Math.log(2);

            result = result - ( px * logPx );
        }

        return result;
    }

    private static void zipArchive(File inputDir, File outputZipFile){
//        outputZipFile.getParentFile().mkdirs();
        String inputDirPath = inputDir.getAbsolutePath();
        byte[] buffer = new byte[1024];

        try (FileOutputStream fileOs = new FileOutputStream(outputZipFile);
             ZipOutputStream zipOs = new ZipOutputStream(fileOs)) {
            String filePath = inputDirPath;
            String entryName = filePath.split("\\\\")[filePath.split("\\\\").length-1];
            ZipEntry ze = new ZipEntry(entryName);
            zipOs.putNextEntry(ze);
            FileInputStream fileIs = new FileInputStream(filePath);
            int len;
            while ((len = fileIs.read(buffer)) > 0) {
                zipOs.write(buffer, 0, len);
            }
            fileIs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
