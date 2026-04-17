package tests;

import fractal.StochasticSymbol;
import fractal.OldSymbol;
import java.util.ArrayList;
import java.util.List;

public class SymbolTester {

    public static void main(String[] args) {
        test();
    }

    public static void test() {
        System.out.println("Testing Symbol class...");
        ArrayList<StochasticSymbol> symbols = new ArrayList<>();
        String str = "";
        // Creating list of symbols and list of chars to compare them
        for (int i = 0; i < 10; i++) {
            char c = (char) ('a' + Math.random() * 26);
            symbols.add(StochasticSymbol.getInstance(c));
            str += c;
        }
        List<StochasticSymbol> prod = StochasticSymbol.getProductionFromString(str);
        for (int i = 0; i < symbols.size(); i++) {
            assert symbols.get(i) == prod.get(i);
        }
        System.out.println("Test passed.");
    }

}
