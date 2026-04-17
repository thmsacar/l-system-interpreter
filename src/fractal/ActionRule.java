package fractal;

import java.util.Arrays;
import java.util.function.Consumer;

import logger.Logger;
import turtle.Turtle;

/**
 * A class representing an action to be executed by String
 * Excepted actions: move dist, left ang, right ang, push, pop
 */
public class ActionRule {

    private Consumer<Turtle> action;

    /**
     * Constructor for an ActionRule. Use the following patterns for a successful interpretation:  <br>
     * | move {distance}  <br>
     * | left {angle}  <br>
     * | right {angle}  <br>
     * | push  <br>
     * | pop  <br>
     * @param act
     */
    public ActionRule (String act) {
        this.action = actionParser(act);
    }

    /**
     * Method parsing the given action.
     * @param act Action command. It should follow pre-defined pattern.
     * @see ActionRule#ActionRule(String) For patterns of actions.
     * @return Consumer representing the action to be executed
     */
    private static Consumer<Turtle> actionParser (String act) {
        String[] words = act.trim().split(" ");
        Consumer<Turtle> action;
        if (words.length == 0 || words.length > 2) {
            Logger.getLogger().error("Invalid action rule: " + act);
            System.exit(1);
        }

        switch (words[0]) {
            case "move":
                action = t -> t.move(getDoubleFromWord(words));
                break;
            case "left":
                action = t -> t.turnLeft(getDoubleFromWord(words));
                break;
            case "right":
                action = t -> t.turnRight(getDoubleFromWord(words));
                break;
            case "push":
                action = t -> t.push();
                break;
            case "pop":
                action = t -> t.pop();
                break;
            case "up":
                action = t->t.pitchUp(getDoubleFromWord(words));
                break;
            case "down":
                action = t->t.pitchDown(getDoubleFromWord(words));
                break;
            case "rollL":
                action = t->t.rollLeft(getDoubleFromWord(words));
                break;
            case "rollR":
                action = t->t.rollRight(getDoubleFromWord(words));
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + words[0]);
        }
        return action;
    }

    /**
     * Gets double from given action
     * @param words action split via white spaces
     * @return the numeric value from the action
     */
    public static Double getDoubleFromWord(String[] words) {
        if (words.length > 1) {
            try {
                return Double.parseDouble(words[1]);
            } catch (Exception e) {
                Logger.getLogger().error("For action " + words[0] + " second argument must be Double: " + words[1], e);
                System.exit(1);
            }
        } else {
            Logger.getLogger().error("There is only 1 argument: " + Arrays.toString(words) + ". There must be 2 arguments for this action.");
            System.exit(1);

        }
        return 0.0;
    }

    /**
     * Executes the pre-defined action of the instance
     * @param turtle Turtle instance to interpret the action. The interpretation may vary depending on the instance given (2D or 3D drawing)
     */
    public void execute (Turtle turtle) {
        this.action.accept(turtle);
    }
    
}
