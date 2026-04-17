package fractalold;

import java.util.HashMap;

import java.util.Map;


/**
 */
@Deprecated
public abstract class Fractal {

    protected int stepSize = 5;
    private String axiom;
    private double angleLeft;
    private double angleRight;
    protected Map<Character, String> rules;

    public Fractal(String chain, double angleLeft, double angleRight, Rule rule) {
        this.axiom = chain;
        this.angleLeft = angleLeft;
        this.angleRight = angleRight;
    }

    public Fractal(String axiom, double angle, Rule rule) {
        this.axiom = axiom;
        this.angleLeft = angle;
        this.angleRight = angle;
        this.rules = new HashMap<>();
    }

    public Fractal(String axiom, double angle, Map<Character, String> rules) {
        this.axiom = axiom;
        this.angleLeft = angle;
        this.angleRight = angle;
        this.rules = rules;
    }


    /**
     *
     * This method produces next step in the sequence using defined rules of the interpreter
     * produces the next step of sequence
     * @return next step in the sequence
     */
    private String evolveAux(String chain) {
        //This method can evolve according to multiple rules at the same time with using same priority for all the rules.
        // String is not modified, a new String is created. Only one letter at a time is calculated.
        // Thus, there is no conflict in production.

        //StringBuilder used for efficient memory management
        StringBuilder result = new StringBuilder();
        for (char c : chain.toCharArray()) {
            String replacement = this.rules.get(c);
            if (replacement != null) {
                result.append(replacement);
            }else {
                result.append(c);
            }
        }
        return result.toString();
    }



    public String evolve() {
        return evolveAux(this.axiom);
    }

    public String evolve (int iterations) {
        String result = this.axiom;
        for (int i = 0; i < iterations; i++) {
            result = this.evolveAux(result);
        }
        return result;
    }

    public String getAxiom() {
        return axiom;
    }

    public void setAxiom(String axiom) {
        this.axiom = axiom;
    }

    public double getAngleLeft() {
        return angleLeft;
    }

    public void setAngleLeft(double angleLeft) {
        this.angleLeft = angleLeft;
    }

    public double getAngleRight() {
        return angleRight;
    }

    public void setAngleRight(double angleRight) {
        this.angleRight = angleRight;
    }

    public void setAngle (double angle) {
        this.angleLeft = angle;
        this.angleRight = angle;
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public Map<Character, String> getRules() {
        return rules;
    }

    public void setRules(Map<Character, String> rules) {
        this.rules = rules;
    }
}
