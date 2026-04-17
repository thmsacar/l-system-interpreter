package fractalold;

import java.util.HashMap;

@Deprecated
public class SierpinskyCurveFractal extends Fractal {

    private static HashMap<Character, String> rules = new HashMap<>();
    {
        rules.put('A', "B-A-B");
        rules.put('B', "A+B+A");
    }

    public SierpinskyCurveFractal(String chain) {
        super(chain, 60.0, rules);
    }


}