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
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor public class GameIORender {
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
    private <T extends Enum<T>> T selectOption(
        String name, T[] values, RandomGenerator randomGenerator
    ) {
        outputInterface.print("Select " + name + ".");

        IntStream.range(0, values.length).forEach(i -> outputInterface.print(i + " - " + values[i].toString()));

        outputInterface.print("other - random.");

        int choice;
        try {
            choice = Integer.parseInt(inputInterface.read());
            if (choice < 0 || choice >= values.length) {
                choice = randomGenerator.nextInt(values.length);
            }
        } catch (NumberFormatException e) {
            choice = randomGenerator.nextInt(values.length);
        }

        outputInterface.print("Your choice " + name + ": " + values[choice].toString());
        return values[choice];
    }

    /**
     * Метод для выбора типа провайдера для ячеек лабиринта.
     * Предлагает пользователю выбрать между простым и продвинутым типами ячеек.
     *
     * @return Выбранный тип провайдера для ячеек.
     */
    public MazeTypeProviderType selectMazeTypeProvider() {
        return selectOption("Maze Type Provider", MazeTypeProviderType.values(), randomGenerator);
    }

    /**
     * Метод для выбора алгоритма генерации лабиринта.
     * Предлагает пользователю выбрать один из доступных алгоритмов.
     *
     * @return Выбранный алгоритм генерации лабиринта.
     */
    public MazeGeneratorType selectMazeGeneratorType() {
        return selectOption("Maze Generator", MazeGeneratorType.values(), randomGenerator);
    }

    /**
     * Метод для выбора алгоритма решения лабиринта.
     * Пользователь выбирает между различными доступными алгоритмами поиска пути.
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
     * Если ввод некорректен, выводит сообщение об ошибке и возвращает пустой Optional.
     *
     * @return Optional с введённым целым числом, или пустой Optional при ошибке.
     */
    private Optional<Integer> readInt() {
        try {
            String input = inputInterface.read();
            return Optional.of(Integer.valueOf(input));
        } catch (NumberFormatException e) {
            outputInterface.print("Invalid input. Please enter a valid number.");
        }
        return Optional.empty();
    }

    /**
     * Метод для чтения целого числа с несколькими попытками ввода и проверкой диапазона.
     * Если все попытки исчерпаны, выбирается случайное значение в заданном диапазоне.
     *
     * @param numberAttempt Количество попыток ввода.
     * @param minNumber     Минимальное допустимое значение.
     * @param maxNumber     Максимальное допустимое значение.
     * @return Optional с введённым числом или случайное значение, если все попытки неудачны.
     */
    public Integer readIntWithRetries(int numberAttempt, int minNumber, int maxNumber, String msg) {
        outputInterface.print(msg);
        for (int i = 0; i < numberAttempt; i++) {
            Optional<Integer> result = readInt();
            if (result.isPresent()) {
                int value = result.orElseThrow();  // Вместо get() используйте orElseThrow
                if (value < minNumber || value > maxNumber) {
                    outputInterface.print("Invalid input. Please enter a number in range from "
                                          + minNumber + " to " + maxNumber + ".");
                } else {
                    return value;
                }
            }
        }
        outputInterface.print("The coordinate will be selected by program.");
        return randomGenerator.nextInt(minNumber, maxNumber);
    }

    /**
     * Метод для чтения координат с несколькими попытками ввода.
     * Пользователь вводит координаты строки и столбца, если попытки неудачны, выбираются случайные значения.
     *
     * @param numberAttempt Количество попыток ввода.
     * @param minNumber     Минимальное значение для координат.
     * @param width         Максимальное значение для ширины.
     * @param height        Максимальное значение для высоты.
     * @return Объект Coordinate с введёнными или случайно выбранными координатами.
     */
    public Coordinate readCoordinates(int numberAttempt, int minNumber, int width, int height) {
        int row = readIntWithRetries(numberAttempt, minNumber, height,
            "Enter row coordinate (" + numberAttempt + ATTEMPT_MESSAGE);

        int col = readIntWithRetries(numberAttempt, minNumber, width,
            "Enter column coordinate (" + numberAttempt + ATTEMPT_MESSAGE);

        return new Coordinate(row, col);
    }

    /**
     * Метод для чтения логического значения из ввода.
     * Если введённое значение равно 0, возвращается false. В остальных случаях - true.
     *
     * @return true, если введено любое значение, кроме 0, иначе false.
     */
    public boolean readBoolean(String message) {
        outputInterface.print(message);
        Optional<Integer> input = readInt();
        return input.orElse(1) != 0;
    }

    public void print(String string) {
        outputInterface.print(string);
    }
}
