package basics;

import java.util.*;

public class EntropySource {

    private Map<String, Probability> matrix;

    private  Map<String, Integer> syllableMap;

    private Map<String,Double> alphabet;

    private Double sourceSize;

    public EntropySource(String text) {
        alphabet = new HashMap<>();

        char[] chars = text.toCharArray();

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

        sourceSize = (double) text.length();

        matrix = new HashMap<>();

        alphabet.keySet().forEach(key -> {
            matrix.put(key, new Probability(key, alphabet.get(key), sourceSize));
        });


       syllableMap = new HashMap<>();

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
    }

    public Map<String, Probability> getProbabilityMatrix() {
        return matrix;
    }

    public List<String> getAlphabet() {

        return new ArrayList<>(alphabet.keySet());
    }

    public Double getSourceSize() {
        return sourceSize;
    }

    public Map<String, Integer> getSyllableMap() {
        return syllableMap;
    }
}
