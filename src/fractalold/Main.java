package fractalold;

import java.util.HashSet;

@Deprecated

public class Main {

    public static void main(String[] args) {


        Rule r1 = new Rule("A", "AB");
        Rule r2 = new Rule("B", "A");
        HashSet<Rule> rules = new HashSet<>();
        rules.add(r1);
        rules.add(r2);

        // Fractal f1 = new Fractal("A", 90.0, rules);

        // SierpinskyTriangleFractal sf1 = new SierpinskyTriangleFractal("F-G-G");

         KochFractal kf1 = new KochFractal();

        // SierpinskyCurveFractal sf2 = new SierpinskyCurveFractal ("A");

        DragonCurveFractal df1 = new DragonCurveFractal ("F");

        for(int i = 0; i<3; i++){
            System.out.println(kf1.evolve());
        }



    }

}
