package fractalold;

import java.util.HashMap;

@Deprecated
public class SierpinskyTriangleFractal extends Fractal {

    private static HashMap<Character, String> rules = new HashMap<>();
    {
        rules.put('F', "F−G+F+G−F");
        rules.put('G', "GG");
    }

    public SierpinskyTriangleFractal(String chain) {
        super(chain, 120.0, rules);
    }

}
