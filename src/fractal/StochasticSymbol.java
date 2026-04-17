package fractal;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import turtle.Turtle;

/**
 * Class representing a symbol/character of an L-System
 * This class stores Singletons for effective memory usage
 */
@Deprecated
public class StochasticSymbol {

    /**
     * Map storing Singletons
     */
    private static final Map<Character, StochasticSymbol> instances = new HashMap<>();

    /**
     * Character represented by the Symbol instance.
     */
    private final char character;

    /**
     * Production rule used for re-writing. Takes null or empty if there is no production rule for the symbol.
     */
    private List<ProductionRule> productionRules;

    /**
     * Action to be executed by the symbol.
     */
    private ActionRule action;

    /**
     * Constructor for creating a Symbol. Only to be used if an instance for given character is not already instantiated.
     * @param character Character to be represented by Symbol object.
     */
    private StochasticSymbol(char character) {
        this.character = character;
        this.action = null;
        this.productionRules = new ArrayList<>();
    }

    /**
     * Method for getting Singleton for a Symbol
     * @param c Character
     * @return Singleton for the character
     */
    public static StochasticSymbol getInstance(char c) {
        instances.putIfAbsent(c, new StochasticSymbol(c));
        return instances.get(c);
    }

    /**
     * Sets the production rule for the Symbol.
     * @param production successor of the production rule
     */
    public void addProduction(String production, double proba) {
        // this.productionRules.add(new ProductionRule(getProductionFromString(production), proba));
    }

    /**
     * Sets the action rule for the Symbol
     * @see ActionRule#ActionRule(String) The patterns to be passed on param.
     * @param action String representing the action to execute
     */
    public void setAction(String action) {
        if(action == null) {
            this.action = null;
        } else {
            this.action = new ActionRule(action);
        }
    }

    /**
     * Sets the action rule for the Symbol
     * @param action ActionRule instance representing the action to execute
     */
    public void setAction(ActionRule action) {
        this.action = action;
    }

    /**
     * Gets Character represented by the Symbol
     * @return Character represented by the Symbol
     */
    public Character getCharacter() {
        return this.character;
    }

    /**
     * Check if sum of productions probabilities equals 1
     * @return true if sum == 1 else false
     */
    public boolean isCoherent () {
        double sum = 0;
        for (ProductionRule p : this.productionRules) {
            sum += p.getProbability();
        }
        return sum == 1;
    }

    public List<ProductionRule> getRules () {
        return this.productionRules;
    }

    /**
     * @return List of Symbols representing the successor of production rule
     */
    public ProductionRule getRuleAtIndex(int i){
        return this.productionRules.get(i);
    }

    /**
     * Takes with stochastic method a rule using productions probabilities
     * @return a Production in 'productions' attribute List
     */
    public ProductionRule getOneRule() {
        if (this.productionRules == null || this.productionRules.isEmpty()) {
            return null;
        }
        if (this.productionRules.size() == 1) {
            return this.productionRules.get(0);
        }

        double probability = Math.random();
        double totalProbability = 0;
        for (ProductionRule prod : this.productionRules) {
            totalProbability += prod.getProbability();
            if (probability <= totalProbability) {
                return prod;
            }
        }
        return this.productionRules.get(this.productionRules.size() - 1);
    }



    /**
     * Executes the pre-defined action represented by ActionRule if exists.
     * @param turtle Turtle object to interpret the action
     */
    public void execute(Turtle turtle) {
        if (action != null) {
            this.action.execute(turtle);
        }
    }

    public void write() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter("text_output.txt", true))) {
            out.print(this.toString());
        }
    }

    /**
     * Transforms a String into a list of Symbol.
     * @param production Successor of the production rule
     * @return List of Symbols representing the successor of production rule
     */
    public static List<StochasticSymbol> getProductionFromString(String production) {
        // Production: "F+F" -> List<Symbol> object
        List<StochasticSymbol> prodList = new ArrayList<>();
        for (char c : production.toCharArray()) {
            prodList.add(StochasticSymbol.getInstance(c));
        }
        return prodList;
    }

    @Override
    public String toString() {
        return String.valueOf(getCharacter());
    }

    public void setProduction(List<ProductionRule> value) {
        this.productionRules = value;
    }
}