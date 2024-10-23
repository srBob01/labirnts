package backend.academy.input;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ConsoleInputReader implements InputInterface {
    private final Scanner scanner;

    public ConsoleInputReader() {
        this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    @Override
    public String read() {
        return scanner.nextLine();
    }

    @Override
    public void close() {
    }
}
