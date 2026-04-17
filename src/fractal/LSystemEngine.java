package fractal;

import logger.Logger;
import turtle.Turtle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing an LSystemEngine. Used for interpreting an L-System and draws it using a Turtle object.
 */
public class LSystemEngine {

    /** Turtle used for graphical interpretation */
    private final Turtle turtle;

    /** Variable for avoiding multiple error messages during the same execution */
    private boolean errorOccured = false;

    /**
     * Constructor for an LSystemEngine
     * @param turtle A Turtle object to draw an L-System. Depending on the given Turtle, the interpretation changes (2D, 3D)
     */
    public LSystemEngine(Turtle turtle) {
        this.turtle = turtle;
    }

    /**
     * Draws an L-System recursively with given iteration/depth
     * For contextual methods use {@link LSystemEngine#drawForContext}
     * @param axiom axiom of the L-system
     * @param depth number of generations
     */
    public void draw(String axiom, int depth, boolean printText) {
         if (printText) {
             try (FileWriter fw = new FileWriter("text_output.txt", false)) {
                 //We are cleaning the file
             } catch (IOException e) {
                 errorOccured = true;
                 Logger.getLogger().warn("Cannot write the output text to file: " + e.getMessage());
             }
         }
        for (char c : axiom.toCharArray()) {
            Symbol s = Symbol.getInstance(c);
            drawRecursive(s, depth, printText);
        }

    }

    /**
     * An auxiliary evolve method to help {@link LSystemEngine#draw}
     * @param s the Symbol to evolve from
     * @param depth number of generations from current point
     * @param printText if true then the final output will be put as text in a txt file
     */
    private void drawRecursive(Symbol s,int depth, boolean printText) {
        // We are getting a contextual rule if exists
        ProductionRule productionRule = s.getOneRule();

        if (depth == 0 || productionRule == null || productionRule.getProduction().isEmpty()) {
            //If we decided to write the text, we write it to a file.
            if (printText && !errorOccured) {
                try{
                    s.write();
                }catch (IOException e){
                    errorOccured = true;
                    Logger.getLogger().warn("Cannot write the output text to file: " + e.getMessage());
                }
            }
            s.execute(turtle);
            return;
        }

         for (Symbol nextSym : productionRule.getProduction()) {
             drawRecursive(nextSym, depth - 1, printText);
         }
    }

    /**
     * An iterative evolve method that draws the picked final generation of L-system
     * @param axiom the starting axiom
     * @param depth desired number of generations
     * @param printText if true then the final output will be put as text in a txt file
     */
    public void drawForContext(String axiom, int depth, boolean printText) {
        LinkedList<Symbol> generation = new LinkedList<>();
        for (char s : axiom.toCharArray()) {
            generation.add(Symbol.getInstance(s));
        }

        if (printText) {
            try (FileWriter fw = new FileWriter("text_output.txt", false)) {
                //We are cleaning the file
            } catch (IOException e) {
                errorOccured = true;
                Logger.getLogger().warn("Cannot write the output text to file: " + e.getMessage());
            }
        }

        HashMap<Integer, List<Symbol>> evolvedSymbols = new HashMap<>();

        for (int i = 0; i < depth; i++) {
            evolvedSymbols.clear();

            for (int j = 0; j < generation.size(); j++) {
                Symbol s = generation.get(j);

                Symbol left = findLeftContext(generation, j);
                Symbol right = findRightContext(generation, j);

                ProductionRule rule = s.getProductionRule(left, right);

                if (rule != null && !rule.getProduction().isEmpty()) {
                    evolvedSymbols.put(j, rule.getProduction());
                }
            }

            List<Integer> sortedKeys = new ArrayList<>(evolvedSymbols.keySet());
            Collections.sort(sortedKeys);

            int decalage = 0;
            for (Integer index : sortedKeys) {
                generation.remove(index + decalage);
                List<Symbol> replacement = evolvedSymbols.get(index);
                generation.addAll(index + decalage, replacement);
                decalage += replacement.size() - 1;
            }

        }

        for (Symbol s : generation) {
            if (printText && !errorOccured) {
                try{
                    s.write();
                }catch (IOException e){
                    errorOccured = true;
                    Logger.getLogger().warn("Cannot write the output text to file: " + e.getMessage());
                }
            }
            s.execute(turtle);
        }
        
    }

    /**
     * Finds the nearest alphabetic symbol to the left.
     */
    private Symbol findLeftContext(LinkedList<Symbol> generation, int currentIndex) {
        int ignoreDepth = 0;
        for (int i = currentIndex - 1; i >= 0; i--) {
            char c = generation.get(i).getCharacter();
            if (c == ']') { ignoreDepth++; continue; }
            if (c == '[') { ignoreDepth--; continue; }
            if (ignoreDepth <= 0 && Character.isLetter(c)) {
                return generation.get(i);
            }
        }
        return null;
    }

    /**
     * Finds the nearest alphabetic symbol to the right.
     */
    private Symbol findRightContext(LinkedList<Symbol> generation, int currentIndex) {
        int ignoreDepth = 0;
        for (int i = currentIndex + 1; i < generation.size(); i++) {
            char c = generation.get(i).getCharacter();
            if (c == '[') { ignoreDepth++; continue; }
            if (c == ']') { ignoreDepth--; continue; }
            if (ignoreDepth == 0 && Character.isLetter(c)) {
                return generation.get(i);
            }
        }
        return null;
    }

} 

