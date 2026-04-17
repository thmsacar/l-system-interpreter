package logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger that prints important events on terminal and writes on a txt file
 */
public class Logger {

    private static final String FICHIER_LOG = "log.txt";
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Singleton
    private static final Logger LOGGER = new Logger();

    /**
     * Private constructor for creation of logger
     */
    private Logger() {
        
        try {
            File fichier = new File(FICHIER_LOG);
            
            
            File dossierParent = fichier.getParentFile();
            if (dossierParent != null && !dossierParent.exists()) {
                dossierParent.mkdirs(); // Force la création du dossier
            }
            
            
            if (!fichier.exists()) {
                fichier.createNewFile(); 
            }
            
           
            new FileWriter(fichier, false).close();
            
            
            System.out.println("[SYSTEME] Log file is created : " + fichier.getAbsolutePath());
            
        } catch (IOException e) {
            System.err.println("Impossible to create log file : " + e.getMessage());
        }
    }
    /**
     * Gets a singleton of Logger
     */
    public static Logger getLogger() {
        return LOGGER;
    }

    /**
     * Log an info message
     * @param message The info message
     */
    public void info(String message) {
        System.out.println("[INFO] " + message);
        writeFile("INFO", message);
    }

    /**
     * Log a warn message
     * @param message The warning message
     */
    public void warn(String message) {
        System.out.println("[ATTENTION] " + message);
        writeFile("ATTENTION", message);
    }

    /**
     * Log an error message
     * @param message The error message
     */
    public void error(String message) {
        System.err.println("[ERROR] " + message);
        writeFile("ERROR", " !!! " + message);
    }
    
    /**
     * Log an error
     *  @param message Error message
     *  @param e Exception that reasons the error
     */
    public void error(String message, Exception e) {
        System.err.println("[ERROR] " + message + " - " + e.getMessage());
        writeFile("ERROR", " !!! " +  message + " | Exception: " + e.getMessage());
    }

    /**
     * Write on log file
     * @param niveau Décris le niveau de gravité (info, warn, error)
     * @param message log un message
     */
    private void writeFile(String niveau, String message) {
        try (FileWriter fw = new FileWriter(FICHIER_LOG, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            String dateHeure = LocalDateTime.now().format(FORMAT_DATE);
            String ligne = dateHeure + " [" + niveau + "] " + message;
            
            bw.write(ligne);
            bw.newLine(); 

        } catch (IOException e) {
            System.err.println("Critical problem : Logger object cannot write in log file !");
        }
    }
}