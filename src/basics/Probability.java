package basics;

public class Probability {

    private String letter;

    private Double a;

    private Double b;

    public double getProbability() {
        return a/b;
    }

    public Probability(){}

    public Probability(String letter, Double a, Double b) {
        this.letter = letter;
        this.a = a;
        this.b = b;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public Double getA() {
        return a;
    }

    public void setA(Double a) {
        this.a = a;
    }

    public Double getB() {
        return b;
    }

    public void setB(Double b) {
        this.b = b;
    }
}
