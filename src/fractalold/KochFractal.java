package fractalold;

import java.util.HashMap;
@Deprecated
public class KochFractal extends Fractal {

    private static HashMap<Character, String> rules = new HashMap<>();
    static {
        rules.put('F', "F+F--F+F");
    }

    public KochFractal() {
        super("F--F--F", 60.0, rules);
    }

}