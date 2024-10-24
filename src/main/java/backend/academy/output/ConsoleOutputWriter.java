package backend.academy.output;

import java.io.PrintStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleOutputWriter implements OutputInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleOutputWriter.class);
    private final PrintStream out;

    public ConsoleOutputWriter() {
        this.out = System.out;
    }

    @Override
    public void print(String output) {
        try {
            out.println(output);
            LOGGER.info("Output to console: {}", output);
        } catch (Exception e) {
            LOGGER.error("Error printing to console", e);
            throw new RuntimeException("Console output error", e);
        }
    }
}
