package tests;

import fractal.StochasticSymbol;
import fractal.ProductionRule;

/**
 * Class to test distribution of StochasticSymbol's production rule
 */
public class StochasticDistributionTester {
    public static void main(String[] args) {
        testProbability();
    }

    public static void testProbability() {
        System.out.println("Testing Stochastic Distribution (10,000 iterations)...");

        StochasticSymbol s = StochasticSymbol.getInstance('S');
        s.addProduction("A", 0.5);
        s.addProduction("B", 0.5);

        int countA = 0;
        int iterations = 10000;

        for (int i = 0; i < iterations; i++) {
            if (s.getOneRule().getProduction().get(0).getCharacter() == 'A') {
                countA++;
            }
        }

        // We are expecting A to be between 4500-5500 after 10000 tries
        System.out.println("Count A: " + countA + " / " + iterations);
        assert countA > 4500 && countA < 5500;

        System.out.println("Stochastic Distribution Test passed.");
    }
}