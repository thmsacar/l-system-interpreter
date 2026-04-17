package fractal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import turtle.Turtle;

@Deprecated
/**
 * Class representing a symbol/character of an L-System
 * This class stores Singletons for effective memory usage
 */
public class OldSymbol {

    /**
     * Map storing Singletons
     */
    private static final Map<Character, OldSymbol> instances = new HashMap<>();

    /**
     * Character represented by the Symbol instance.
     */
    private final char character;

    /**
     * Production rule used for re-writing. Takes null or empty if there is no production rule for the symbol.
     */
    private List<OldSymbol> production;

    /**
     * Action to be executed by the symbol.
     */
    private ActionRule action;


    /**
     * Constructor for creating a Symbol. Only to be used if an instance for given character is not already instantiated.
     * @param character Character to be represented by Symbol object.
     */
    private OldSymbol(char character) {
        this.character = character;
        this.action = null;
        this.production = null;
    }

    /**
     * Method for getting Singleton for a Symbol
     * @param c Character
     * @return Singleton for the character
     */
    public static OldSymbol getInstance(char c) {
        instances.putIfAbsent(c, new OldSymbol(c));
        return instances.get(c);
    }

    /**
     * Sets the production rule for the Symbol.
     * @param production successor of the production rule
     */
    public void setProduction(String production) {
        this.production = getProductionFromString(production);
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
     * Executes the pre-defined action represented by ActionRule if exists.
     * @param turtle Turtle object to interpret the action
     */
    public void execute(Turtle turtle) {
        if (action != null) {
            this.action.execute(turtle);
        }
    }

    /**
     * @return List of Symbols representing the successor of production rule
     */
    public List<OldSymbol> getRule(){
        return this.production;
    }


    /**
     * Transforms a String into a list of Symbol.
     * @param production Successor of the production rule
     * @return List of Symbols representing the successor of production rule
     */
    public static List<OldSymbol> getProductionFromString(String production) {
        // Production: "F+F" -> List<Symbol> object
        List<OldSymbol> prodList = new ArrayList<>();
        for (char c : production.toCharArray()) {
            prodList.add(OldSymbol.getInstance(c));
        }
        return prodList;
    }

    @Override
    public String toString() {
        return String.valueOf(getCharacter());
    }
}