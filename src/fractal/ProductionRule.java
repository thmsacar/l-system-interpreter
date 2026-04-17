package fractal;

import logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a production {@code List<Symbol>} with its probability (double)
 */
public class ProductionRule {

    /** The production of this rule as {@code List<Symbol>} */
    private List<Symbol> production;

    /** Probability of occurence between 0 and 1. For deterministic rules 1 should be used */
    private double probability;

    /** Required left context to match this rule. \0 represents no context */
    private char leftContext = '\0';

    /** Required right context to match this rule. \0 represents no context */
    private char rightContext = '\0';

    /**
     * Constructor for ProductionRule
     * @param production the production rule as {@code List<Symbol>}
     * @param probability probability of occurence between 0 and 1
     */
    public ProductionRule(List<Symbol> production, double probability) {
        this.production = production;
        this.setProbability(probability);
    }

    /**
     * Constructor for ProductionRule
     * @param production the production rule as {@code String}
     * @param probability probability of occurence between 0 and 1
     */
    public ProductionRule(String production, double probability) {
        this.production = getProductionFromString(production);
        this.setProbability(probability);
    }

    /**
     * Gets the production as a List of {@link Symbol}
     * @return
     */
    public List<Symbol> getProduction() {
        return this.production;
    }

    /**
     * Gets the probability of occurrence
     * @return
     */
    public double getProbability() {
        return this.probability;
    }

    /**
     * Sets the production of the rule
     * @param production
     */
    public void setProduction(List<Symbol> production) {
        this.production = production;
    }

    /**
     * Sets the production of the rule
     * @param production
     */
    public void setProduction(String production) {this.production = getProductionFromString(production);}

    /**
     * Sets the probability of the rule
     * @param probability
     */
    public void setProbability(double probability) {
        if (probability < 0 || probability > 1) {
            Logger.getLogger().error("Probability must be between 0 and 1. It was " + probability);
            System.exit(1);
        }
        this.probability = probability;
    }

    /**
     * Sets the context of the rule. '\0' means no context
     * @param left
     * @param right
     */
    public void setContext (char left, char right) {
        this.leftContext = left;
        this.rightContext = right;
    }

    /**
     * Method to tell if rule is contextual
     * @return true if a context is set for this rule
     */
    public boolean isContextual () {
        return this.leftContext != '\0' || this.rightContext != '\0';
    }

    /** Returns left context */
    public char getLeftContext()  { return this.leftContext; }
    /** Returns right context */
    public char getRightContext() { return this.rightContext; }

    /**
     * Checks if the context is matched for this rule
     * @param left the Symbol on the left
     * @param right the Symbol on the right
     * @return true if context is matched
     */
    public boolean matches (Symbol left, Symbol right) {
        boolean leftMatch = (this.leftContext == '\0' || (left != null && left.getCharacter() == this.leftContext));
        boolean rightMatch = (this.rightContext == '\0' || (right != null && right.getCharacter() == this.rightContext));

        return leftMatch && rightMatch;
    }

    /**
     * Transforms a String into a list of Symbol.
     * @param production Successor of the production rule
     * @return List of Symbols representing the successor of production rule
     */
    public static List<Symbol> getProductionFromString(String production) {
        // Production: "F+F" -> List<Symbol> object
        List<Symbol> prodList = new ArrayList<>();
        for (char c : production.toCharArray()) {
            prodList.add(Symbol.getInstance(c));
        }
        return prodList;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        for (Symbol s : this.production) {
            sb.append(s.toString());
        }
        String str = String.format("%s:%.3f", sb, this.getProbability());
        return str;
    }

}
