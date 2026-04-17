package configuration;

import logger.Logger; // <-- On importe TON Logger !

import fractal.ProductionRule;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used for loading configuration of the L-System.
 * Loads the axiom, production rules, action rules.
 */
public class Configuration {

    private String axiom;
    /**
     * Map String, String representing production rules of an L-System. Each key, value pair is a production rule such that: key -> value.
     * Which means key will be rewritten as value after a single iteration.
     */
    private Map<Character, List<ProductionRule>> productionRules = new HashMap<>();
    private Map<Character, String> actionRules = new HashMap<>();
    private int iteration;
    private String filePath ;

    // Compilation du pattern une seule fois au début
    // ^(.+?)      -> Capture n'importe quel caractère (F, +, [, etc.)
    // \\s*[:=]\\s* -> Accepte soit ":" soit "=" comme séparateur
    // (.*)$       -> Capture le reste de la ligne
    private final Pattern PATTERN; 

    public Configuration(String filePath){
        this.filePath = filePath;
        this.PATTERN = Pattern.compile("^(.+?)\\s*[:=]\\s*(.*)$");
    }

    private BufferedReader openFile() throws FileNotFoundException{
        return new BufferedReader(new FileReader(this.filePath)); 
    }

    public void loadConfig() {
        Logger.getLogger().info("Opening configuration file : " + filePath);
        
        try (BufferedReader bufferedReader = openFile()) {
            Logger.getLogger().info("File opened, treating lines...");
            processLines(bufferedReader, this.PATTERN);
            Logger.getLogger().info("Finished treating lines.");
            
        } catch (FileNotFoundException e) {
            // Utilisation de la méthode error() avec l'exception pour avoir les détails dans le log
            Logger.getLogger().error("Configuration file could not be found : " + filePath, e);
            System.exit(1);
        } catch (IOException e) {
            Logger.getLogger().error("Error during reading file : " + filePath, e);
            System.exit(1);
        }
    }

    private void processLines(BufferedReader bufferedReader, Pattern pattern) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            
            String cleanLine = line.trim();
            // On ignore les lignes vides ou les commentaires (commençant par #)
            if (cleanLine.isEmpty() || cleanLine.startsWith("#")) continue;
            
            Matcher matcher = pattern.matcher(cleanLine);

            if (matcher.find()) {
                
                String key = matcher.group(1).trim().toLowerCase(); 
                String value = matcher.group(2).trim();

             
                switch (key) {
                    case "axiom":
                    case "axiome":
                        this.axiom = value;
                        break;

                    case "rule":
                    case "regle":
                    case "règle":
                        String[] parties = value.split(",");
                        if (parties.length == 2) {
                            String contextSpec = parties[0].trim();
                            String prodRaw = parties[1].trim();
                            char predecessor;
                            char leftCtx = '\0';
                            char rightCtx = '\0';
                            Pattern ctxPattern = Pattern.compile("(?:([^\\s,<>]+)\\s*<\\s*)?([^\\s,<>]+)(?:\\s*>\\s*([^\\s,<>]+))?");
                            Matcher ctxMatcher = ctxPattern.matcher(contextSpec);

                            if (ctxMatcher.find()) {
                                if (ctxMatcher.group(1) != null) leftCtx = ctxMatcher.group(1).charAt(0);
                                predecessor = ctxMatcher.group(2).charAt(0);
                                if (ctxMatcher.group(3) != null) rightCtx = ctxMatcher.group(3).charAt(0);
                            } else {
                                predecessor = contextSpec.charAt(0);
                            }

                            String[] raw = prodRaw.split(":");
                            String rule = raw[0].trim();
                            double proba = 1;
                            if (raw.length == 2) {
                                try {
                                    proba = Double.parseDouble(raw[1]);
                                } catch (NumberFormatException e) {
                                    Logger.getLogger().error("Error while parsing probability: " + raw[1], e);
                                    System.exit(1);
                                }
                            }

                            ProductionRule prod = new ProductionRule(rule, proba);
                            prod.setContext(leftCtx, rightCtx);
                            List<ProductionRule> listProd = productionRules.getOrDefault(predecessor, new ArrayList<>());
                            listProd.add(prod);
                            productionRules.put(predecessor, listProd);
                        } else {
                            Logger.getLogger().warn("Format of rule is invalid (ignored) : " + cleanLine);
                        }
                        break;

                    case "action":
                        String[] partiesAction = value.split(",");
                        if (partiesAction.length >= 2) { 
                            String symbolStr = partiesAction[0].trim();
                            if (!symbolStr.isEmpty()) {
                                char symbole = symbolStr.charAt(0);
                                String commande = partiesAction[1].trim();
                                actionRules.put(symbole, commande);
                            }
                        } else {
                            Logger.getLogger().warn("Format of action is invalid (ignored) : " + cleanLine);
                        }
                        break;
                    case "iteration":
                        try {
                            this.iteration = Integer.parseInt(value);
                        } catch (Exception e) {
                            Logger.getLogger().error("Format of depth (iteration) is invalid : '" + value );
                            System.exit(1);
                        }
                        break;
                    default:
                        // Utilisation du niveau WARN (Avertissement) pour une clé inconnue
                        Logger.getLogger().warn("Unknown key is ignored -> " + key);
                }
            } else {
                // WARN si la ligne ne ressemble pas à "clé = valeur"
                Logger.getLogger().error("Line does not respect to the expected format -> " + cleanLine);
                System.exit(1);
            }
        }
    }

    // --- GETTERS ---

    /**
     * Gets Map of Character, String representing action rules. Key represents a symbol, value is the action.
     */
    public Map<Character, String> getActionRules() { return this.actionRules; }

    /**
     * Gets Map of Character, String representing production rules. Key is the predecessor, value is the successor
     */
    public Map<Character, List<ProductionRule>> getProductionRules() { return this.productionRules; }
    
    public int getIteration(){
        return this.iteration;
    }
    
    public String getAxiom() { return this.axiom; }

    public void printConfig() {
        Logger.getLogger().info("--- Configuration loaded ---");
        Logger.getLogger().info("Axiom     : " + this.axiom);
        Logger.getLogger().info("Iteration : " + this.iteration);
        Logger.getLogger().info("Actions   : " + this.actionRules);
        Logger.getLogger().info("Rules :");
        for (Map.Entry<Character, List<ProductionRule>> entry : this.productionRules.entrySet()) {
            for (ProductionRule rule : entry.getValue()) {
                if (rule.isContextual()) {
                    String left  = (rule.getLeftContext()  != '\0') ? String.valueOf(rule.getLeftContext())  : "_";
                    String right = (rule.getRightContext() != '\0') ? String.valueOf(rule.getRightContext()) : "_";
                    Logger.getLogger().info("  " + entry.getKey() + " : [contextuelle] " + left + " < " + entry.getKey() + " > " + right + "  ->  " + rule);
                } else {
                    Logger.getLogger().info("  " + entry.getKey() + " : [standard]     ->  " + rule);
                }
            }
        }
    }
}