import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The Logger class provides a simple logging mechanism.
 */
public class Logger {
    private static final String LOG_DIRECTORY = "logs";
    private static final DateTimeFormatter FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * Logs a message with the specified log level.
     *
     * @param level   the log level
     * @param message the log message
     */
    public static void log(String level, String message) {
        LocalDate currentDate = LocalDate.now();
        String fileName = LOG_DIRECTORY + "/log-" + currentDate.format(FILE_NAME_FORMATTER) + ".txt";

        try {
            Files.createDirectories(Paths.get(LOG_DIRECTORY)); // Create the logs directory if it doesn't exist
            try (FileWriter fileWriter = new FileWriter(fileName, true)) {
                fileWriter.write(currentDate + " [" + level + "] " + message + "\n");
            }
        } catch (IOException e) {
            System.err.println("Logging Error: " + e.getMessage());
        }
    }
}
