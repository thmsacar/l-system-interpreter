package fractal;

import configuration.Configuration;
import logger.Logger;
import turtle.Turtle;
import turtle.Turtle2D;
import turtle.Turtle3D;

import java.util.List;
import java.util.Map;

/**
 * Main class that runs the program
 */
public class Main {

    public static void main (String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: java --add-exports=java.desktop/sun.awt=ALL-UNNAMED --add-opens=java.desktop/sun.awt=ALL-UNNAMED -jar Project-Lindemeyer.jar <path> <type>");
            System.exit(1);
        }

        String path = args[0];
        String type = args[1].trim().toLowerCase();

        boolean printText = false;
        if (args.length>=3 && args[2].trim().toUpperCase().equals("Y")){
            printText = true;
        }

    //    String path = "config.txt";
    //    String type = "2d";

        Configuration config = new Configuration(path);
        config.loadConfig();
        config.printConfig();


        Turtle turtle;
        switch (type){
            case "2d":
                turtle = new Turtle2D();
                break;
            case "3d":
                turtle = new Turtle3D();
                break;
            default:
                turtle = new Turtle2D();
                break;
        }


        int depth = config.getIteration();

        String axiom =  config.getAxiom();
        Map<Character, String>  actions = config.getActionRules();
        Map<Character, List<ProductionRule>> productions = config.getProductionRules();


        for (Map.Entry<Character, String> a : actions.entrySet()) {
            Symbol.getInstance(a.getKey()).setAction(a.getValue());
        }

        for (Map.Entry<Character, List<ProductionRule>> p : productions.entrySet()) {
            Symbol.getInstance(p.getKey()).setProduction(p.getValue());
        }

        LSystemEngine engine = new LSystemEngine(turtle);


        long startTime = System.currentTimeMillis();
        if(Symbol.anyContextual()){
            Logger.getLogger().info("Contextual symbol spotted. Drawing iterative.");
            engine.drawForContext(axiom, depth, printText);
        } else {
            Logger.getLogger().info("No contextual symbol. Drawing recursive.");
            engine.draw(axiom, depth, printText);
        }
        long endTime = System.currentTimeMillis();
        Logger.getLogger().info("End of generation. Elapsed time: " + (endTime - startTime) + "ms");

    }

}