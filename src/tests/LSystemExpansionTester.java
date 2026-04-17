package tests;

import fractal.Symbol;
import fractal.ProductionRule;
import java.util.List;
import java.util.ArrayList;

/**
 * Class to test production of LSystem
 */
public class LSystemExpansionTester {
    public static void main(String[] args) {
        testExpansion();
    }

    public static void testExpansion() {
        System.out.println("Testing L-System Expansion...");

        Symbol f = Symbol.getInstance('F');
        Symbol plus = Symbol.getInstance('+');
        f.addProduction("F+F", 1.0);

        ProductionRule rule = f.getOneRule();
        List<Symbol> result = rule.getProduction();

        assert result.size() == 3;
        assert result.get(0) == f;
        assert result.get(1) == plus;
        assert result.get(2) == f;

        System.out.println("L-System Expansion Test passed.");
    }
}