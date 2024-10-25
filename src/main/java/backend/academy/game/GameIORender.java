package backend.academy.game;

import backend.academy.cycleadder.CycleLevelType;
import backend.academy.entity.cell.Coordinate;
import backend.academy.generator.MazeGeneratorType;
import backend.academy.input.InputInterface;
import backend.academy.mazetype.MazeTypeProviderType;
import backend.academy.output.OutputInterface;
import backend.academy.random.RandomGenerator;
import backend.academy.solver.SolverType;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor public class GameIORender {

    private static final Logger LOGGER = Logger.getLogger(GameIORender.class.getName());

    private static final String ATTEMPT_MESSAGE = " attempt): ";
    private final InputInterface inputInterface;
    private final OutputInterface outputInterface;
    private final RandomGenerator randomGenerator;

    /**
     * Универсальный метод для выбора опции из перечисления с возможностью выбора случайной опции.
     *
     * @param name            Название параметра, который выбирается.
     * @param values          Массив доступных значений перечисления.
     * @param randomGenerator Генератор случайных чисел для случайного выбора опции.
     * @param <T>             Тип перечисления.
     * @return Выбранное значение перечисления.
     */
    private <T extends Enum<T>> T selectOption(String name, T[] values, RandomGenerator randomGenerator) {
        outputInterface.print("Select " + name + ".");
        LOGGER.info("Prompting user to select " + name);

        IntStream.range(0, values.length).forEach(i -> outputInterface.print(i + " - " + values[i].toString()));

        outputInterface.print("other - random.");
        int choice;
        try {
            choice = Integer.parseInt(inputInterface.read());
            if (choice < 0 || choice >= values.length) {
                choice = randomGenerator.nextInt(values.length);
                LOGGER.info("Invalid choice, selecting random option for " + name);
            }
        } catch (NumberFormatException e) {
            choice = randomGenerator.nextInt(values.length);
            LOGGER.warning("Invalid number format for choice, selecting random option for " + name);
        }

        outputInterface.print("Your choice " + name + ": " + values[choice].toString());
        LOGGER.info("User selected: " + values[choice].toString());
        return values[choice];
    }

    /**
     * Метод для выбора типа провайдера для ячеек лабиринта.
     *
     * @return Выбранный тип провайдера для ячеек.
     */
    public MazeTypeProviderType selectMazeTypeProvider() {
        return selectOption("Maze Type Provider", MazeTypeProviderType.values(), randomGenerator);
    }

    /**
     * Метод для выбора алгоритма генерации лабиринта.
     *
     * @return Выбранный алгоритм генерации лабиринта.
     */
    public MazeGeneratorType selectMazeGeneratorType() {
        return selectOption("Maze Generator", MazeGeneratorType.values(), randomGenerator);
    }

    /**
     * Метод для выбора алгоритма решения лабиринта.
     *
     * @return Выбранный алгоритм решения лабиринта.
     */
    public SolverType selectSolverType() {
        return selectOption("Solver Type", SolverType.values(), randomGenerator);
    }

    /**
     * Метод для выбора текущего состояния игры
     *
     * @return Выбранный метод
     */
    public GameStateType selectGameState() {
        return selectOption("Game State", GameStateType.values(), randomGenerator);
    }

    /**
     * Метод для выбора уровня добавления цикличности
     *
     * @return Выбранный метод
     */
    public CycleLevelType selectCycleLevelType() {
        return selectOption("Cycle level", CycleLevelType.values(), randomGenerator);
    }

    /**
     * Метод для чтения целого числа из ввода с обработкой исключений.
     *
     * @return Optional с введённым целым числом, или пустой Optional при ошибке.
     */
    private Optional<Integer> readInt() {
        try {
            String input = inputInterface.read();
            LOGGER.info("Reading integer input: " + input);
            return Optional.of(Integer.valueOf(input));
        } catch (NumberFormatException e) {
            outputInterface.print("Invalid input. Please enter a valid number.");
            LOGGER.warning("Invalid number input");
        }
        return Optional.empty();
    }

    /**
     * Метод для чтения целого числа с несколькими попытками ввода и проверкой диапазона.
     * Если все попытки исчерпаны, выбирается случайное значение в заданном диапазоне.
     */
    public Integer readIntWithRetries(int numberAttempt, int minNumber, int maxNumber, String msg) {
        outputInterface.print(msg);
        LOGGER.info("Prompting user for input with retries: " + msg);
        for (int i = 0; i < numberAttempt; i++) {
            Optional<Integer> result = readInt();
            if (result.isPresent()) {
                int value = result.orElseThrow();  // Вместо get() используйте orElseThrow
                if (value < minNumber || value > maxNumber) {
                    outputInterface.print(
                        "Invalid input. Please enter a number in range from " + minNumber + " to " + maxNumber + ".");
                    LOGGER.warning("Input out of range: " + value);
                } else {
                    LOGGER.info("Valid input received: " + value);
                    return value;
                }
            }
        }
        outputInterface.print("The coordinate will be selected by program.");
        int randomValue = randomGenerator.nextInt(minNumber, maxNumber);
        LOGGER.info("All attempts exhausted. Random value selected: " + randomValue);
        return randomValue;
    }

    /**
     * Метод для чтения координат с несколькими попытками ввода.
     */
    public Coordinate readCoordinates(int numberAttempt, int minNumber, int width, int height) {
        LOGGER.info("Reading row coordinate...");
        int row = readIntWithRetries(numberAttempt, minNumber, height,
            "Enter row coordinate (" + numberAttempt + ATTEMPT_MESSAGE);

        LOGGER.info("Reading column coordinate...");
        int col = readIntWithRetries(numberAttempt, minNumber, width,
            "Enter column coordinate (" + numberAttempt + ATTEMPT_MESSAGE);

        LOGGER.info("Coordinates received: (" + row + ", " + col + ")");
        return new Coordinate(row, col);
    }

    /**
     * Метод для чтения логического значения из ввода.
     */
    public boolean readBoolean(String message) {
        outputInterface.print(message);
        LOGGER.info("Reading boolean input: " + message);
        Optional<Integer> input = readInt();
        boolean result = input.orElse(1) != 0;
        LOGGER.info("Boolean input result: " + result);
        return result;
    }

    public void print(String string) {
        outputInterface.print(string);
        LOGGER.info("Output: " + string);
    }
}
