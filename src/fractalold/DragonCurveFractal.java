package fractalold;

import java.util.HashMap;

@Deprecated
public class DragonCurveFractal extends Fractal {

    private static HashMap<Character, String> rules = new HashMap<>();
    {
        rules.put('F', "F+G");
        rules.put('G', "F-G");
    }

    public DragonCurveFractal() {
        this("F");
    }

    public DragonCurveFractal(String chain) {
        super(chain, 90.0, rules);
    }

}