package basics;

public class Calc {

    public static Double entropy(EntropySource src, Integer consideringSymbolsCount) {

        if(consideringSymbolsCount == 1) {
            Double result = 0D;

            for (String s : src.getProbabilityMatrix().keySet()) {

                double px = src.getProbabilityMatrix().get(s).getProbability();
                double logPx = Math.log(src.getProbabilityMatrix().get(s).getProbability())/Math.log(2);

                result = result - ( px * logPx );
            }

            return result;
        }else if (consideringSymbolsCount == 2) {
            Double summ = 0d;

            Double countOfSyllalbels = src.getSourceSize()-1;

            for(String sylalble : src.getSyllableMap().keySet()) {

                Double countOfThisSyllable = src.getSyllableMap().get(sylalble).doubleValue();

                String firstLetter = Character.toString(sylalble.charAt(0));

                double firstLetterProbability = src.getProbabilityMatrix().get(firstLetter).getProbability();



                double underLogExpression = countOfThisSyllable / countOfSyllalbels / firstLetterProbability;

                double log = Math.log(underLogExpression)/Math.log(2.0);

                summ -= (countOfThisSyllable /countOfSyllalbels)*log;

            }
            return summ;
        }else {
            return 0.0;
        }

    }
}
