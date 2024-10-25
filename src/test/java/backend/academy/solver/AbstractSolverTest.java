package backend.academy.solver;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.cell.Coordinate;
import backend.academy.entity.maze.Maze;
import backend.academy.entity.path.Path;
import backend.academy.mazetype.AdvancedMazeTypeProvider;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.random.SimpleRandomGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Абстрактный класс для тестирования алгоритмов поиска пути.
 */
public abstract class AbstractSolverTest {
    private final Solver solver;
    private final boolean shouldFindOptimalPath;
    private final MazeFactory mazeFactory;
    private final MazeTypeProvider mazeTypeProvider;

    public AbstractSolverTest(Solver solver, boolean shouldFindOptimalPath) {
        this.solver = solver;
        this.shouldFindOptimalPath = shouldFindOptimalPath;
        mazeFactory = new MazeFactory();
        mazeTypeProvider = new AdvancedMazeTypeProvider(new SimpleRandomGenerator());
    }

    /**
     * Тест для лабиринта с несколькими возможными путями.
     * <p>
     * Проверяет способность решателя находить оптимальный путь, если он задан как "оптимальный".
     * - Если решатель должен находить оптимальный путь, проверяется, что он находит путь с минимальной стоимостью.
     * - В противном случае, тест проверяет, что найденный путь находится в диапазоне возможных стоимостей.
     */
    @Test
    public void testMazeWithMultiplePaths() {
        // Arrange
        Maze maze = mazeFactory.createMultiplePathsMaze();

        // Act
        Path path = solver.solve(maze, mazeFactory.startCoord(), mazeFactory.endCoord(), mazeTypeProvider);

        // Assert
        if (shouldFindOptimalPath) {
            assertEquals(mazeFactory.pathCostMin(), path.totalCost(), "Должен быть найден оптимальный путь");
        } else {
            assertTrue(
                path.totalCost() <= mazeFactory.pathCostMax() && path.totalCost() >= mazeFactory.pathCostMin(),
                "Путь найден, стоимость в диапазоне");
        }
    }

    /**
     * Тест для непроходимого лабиринта.
     * <p>
     * Проверяет, что решатель корректно определяет отсутствие пути в лабиринте, где нет соединённых ячеек,
     * и возвращает пустой путь.
     */
    @Test
    public void testUnreachableMaze() {
        // Arrange
        Maze maze = mazeFactory.createUnreachableMaze();

        // Act
        Path path = solver.solve(maze, new Coordinate(0, 0), new Coordinate(4, 4), mazeTypeProvider);

        // Assert
        assertTrue(path.coordinates().isEmpty(), "Путь не должен быть найден в непроходимом лабиринте");
    }

    /**
     * Тест для проверки некорректных начальных и конечных координат.
     * <p>
     * Проверяет, что решатель возвращает пустой путь, если начальные или конечные координаты
     * выходят за пределы лабиринта, и не вызывает ошибки или исключения.
     */
    @Test
    public void testInvalidStartEndCoordinates() {
        // Arrange
        Maze maze = mazeFactory.createMultiplePathsMaze();

        // Act
        Path path = solver.solve(maze, new Coordinate(-1, -1),
            new Coordinate(5, 5), mazeTypeProvider);

        // Assert
        assertTrue(path.coordinates().isEmpty(), "Путь не должен быть найден для неверных координат");
    }

    /**
     * Тест для случая совпадения начальной и конечной координат.
     * <p>
     * Проверяет, что если начальная и конечная точки совпадают, решатель возвращает путь,
     * состоящий из единственной ячейки с корректной стоимостью, равной стоимости этой ячейки.
     */
    @Test
    public void testSameStartEndCoordinates() {
        // Arrange
        Maze maze = mazeFactory.createMultiplePathsMaze();
        Cell startCell = maze.getCell(mazeFactory.startCoord());

        // Act
        Path path = solver.solve(maze, mazeFactory.startCoord(), mazeFactory.startCoord(), mazeTypeProvider);

        // Assert
        assertEquals(startCell.cellType().movementCost(), path.totalCost(),
            "Стоимость должна равна стоимости ячейки при совпадении начальной и конечной координат");
        assertEquals(1, path.coordinates().size(), "Путь должен содержать только одну координату");
        assertEquals(startCell.coordinate(), path.coordinates().getFirst(),
            "Путь должен состоять из этой ячейки");
    }
}
