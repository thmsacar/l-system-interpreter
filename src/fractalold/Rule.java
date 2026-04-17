package fractalold;

/**
 * This class represents the production rule of an L-System where the predecessor symbol is mapped to its successor string.
 * Ex: (F->F+F−F−F+F) is a production rule of an L-System.
 */


@Deprecated
public class Rule {

    private String predecessor;
    private String successor;

    /**
     * Constructor of a Rule object.
     * @param predecessor Variable of the rule. Ex: For rule (F->F+F−F−F+F), "F" is the predecessor (variable).
     * @param successor Expression (mapping) of the rule. Ex: For rule (F->F+F−F−F+F), "F+F−F−F+F" is the successor (expression).
     */
    public Rule(String predecessor, String successor) {
        this.successor = successor;
        this.predecessor = predecessor;
    }
    public String getPredecessor() {
        return this.predecessor;
    }

    public void setPredecessor(String predecessor) {
        this.predecessor = predecessor;
    }

    public String getSuccessor() {
        return this.successor;
    }

    public void setSuccessor(String successor) {
        this.successor = successor;
    }


}
