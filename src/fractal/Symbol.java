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
public class Symbol {

    /**
     * Map storing Singletons
     */
    private static final Map<Character, Symbol> instances = new HashMap<>();

    /**
     * Character represented by the Symbol instance.
     */
    private final char character;

    /**
     * Production rules used for re-writing. Takes null or empty if there is no production rule for the symbol.
     * Multiple rules can be used for stochastic or contextual symbols.
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
    private Symbol(char character) {
        this.character = character;
        this.action = null;
        this.productionRules = new ArrayList<>();
    }

    /**
     * Method for getting Singleton for a Symbol
     * @param c Character
     * @return Singleton for the character
     */
    public static Symbol getInstance(char c) {
        instances.putIfAbsent(c, new Symbol(c));
        return instances.get(c);
    }

    /**
     * Sets the production rule for the Symbol.
     * @param production successor of the production rule
     */
    public void addProduction(String production, double proba) {
        this.productionRules.add(new ProductionRule(getProductionFromString(production), proba));
    }

    public void addProduction(ProductionRule production) {
        this.productionRules.add(production);
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


    /**
     * Gets rules assigned to this symbol
     * @return a {@code List} of {@link ProductionRule} defining reproduction rules of this symbol
     */
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
     * Gets a rule using productions probabilities with stochastic method
     * @return a Production in 'productions' attribute List
     */
    public ProductionRule getOneRule() {
        if (this.productionRules == null || this.productionRules.isEmpty()) return null;

        List<ProductionRule> standardRules = this.productionRules.stream().filter(r -> !r.isContextual()).toList();

        if (standardRules.isEmpty()) return null;
        if (standardRules.size() == 1) return standardRules.get(0);

        double probability = Math.random();
        for (ProductionRule prod : standardRules) {
            if (probability <= prod.getProbability()) {
                return prod;
            }
            probability -= prod.getProbability();
        }
        return standardRules.getLast();
    }

    /**
     * Gets a Production rule checking context, if context is not matched then another rule will be picked
     * @param left the Symbol on the left
     * @param right the Symbol on the left
     * @return a ProductionRule assigned to this Symbol
     */
    public ProductionRule getProductionRule (Symbol left, Symbol right) {
        // Check if rules are not empty
        if (this.productionRules == null || this.productionRules.isEmpty()) 
            return null;

        // Check first contextual rules
        for (ProductionRule r : this.productionRules) {
            if (r.isContextual() && r.matches(left, right)) return r;
        }

        // Check stochastic and standard rules
        return getOneRule();
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

    /**
     * Write the current symbol to pre-determined text file
     * @throws IOException
     */
    public void write() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter("text_output.txt", true))) {
            out.print(this.toString());
        }
    }

    /**
     * Checks if Symbol has a contextual rule
     * @return true if at least one contextual rule is assigned to this symbol
     */
    private boolean isContextual() {
        for (ProductionRule p : this.productionRules) {
            if (p.isContextual()) return true;
        }
        return false;
    }

    /**
     * Transforms a String into a list of Symbol.
     * @param production Successor of the production ruleœ
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

    /**
     * Checks whether a contextual Symbol has been instanced already.
     * @return true if at least one Symbol with a contextual rule has been instanced
     */
    public static boolean anyContextual(){
        for (Symbol s : instances.values()) {
            if (s.isContextual()) return true;
        }
        return false;
    }

    /**
     * Sets production rules for this symbol
     * @param value a {@code List} of {@link ProductionRule} defining reproduction rules of this symbol
     */
    public void setProduction(List<ProductionRule> value) {
        this.productionRules = value;
    }


    @Override
    public String toString() {
        return String.valueOf(getCharacter());
    }



}
