package backend.academy.generator;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.maze.Maze;
import backend.academy.mazetype.MazeTypeProvider;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractMazeGeneratorTest {

    protected abstract Generator getGenerator();

    protected abstract MazeTypeProvider getMazeTypeProvider();

    private final boolean isCyclic;
    protected final int HEIGHT = 10;
    protected final int WIDTH = 10;

    protected AbstractMazeGeneratorTest(boolean isCyclic) {
        this.isCyclic = isCyclic;
    }

    /**
     * Тест на проверку связности лабиринта.
     */
    @Test
    public void testMazeConnectivity() {
        // Arrange
        Generator generator = getGenerator();
        MazeTypeProvider typeProvider = getMazeTypeProvider();
        Maze maze = generator.generate(HEIGHT, WIDTH, typeProvider);

        // Act
        boolean isConnected = isMazeConnected(maze);

        // Assert
        assertTrue(isConnected, "Лабиринт должен быть полностью связным");
    }

    /**
     * Тест на проверку ацикличности или цикличности лабиринта в зависимости от типа.
     */
    @Test
    public void testMazeAcyclicOrCyclic() {
        // Arrange
        Generator generator = getGenerator();
        MazeTypeProvider typeProvider = getMazeTypeProvider();
        Maze maze = generator.generate(HEIGHT, WIDTH, typeProvider);

        // Act
        boolean isAcyclic = isMazeAcyclic(maze);

        // Assert
        if (isCyclic) {
            assertFalse(isAcyclic, "Лабиринт должен содержать циклы");
        } else {
            assertTrue(isAcyclic, "Лабиринт должен быть ацикличным");
        }
    }

    /**
     * Тест на проверку включения всех ячеек в лабиринт.
     */
    @Test
    public void testAllCellsIncluded() {
        // Arrange
        Generator generator = getGenerator();
        MazeTypeProvider typeProvider = getMazeTypeProvider();
        Maze maze = generator.generate(HEIGHT, WIDTH, typeProvider);

        // Act
        int cellCount = maze.getAllCells().size();

        // Assert
        assertEquals(HEIGHT * WIDTH, cellCount, "Все ячейки должны быть включены в лабиринт");
    }

    /**
     * Тест на минимальное количество рёбер для ацикличного лабиринта.
     * Для цикличного лабиринта проверяется наличие циклов.
     */
    @Test
    public void testMinimumEdgesForConnectivity() {
        // Arrange
        Generator generator = getGenerator();
        MazeTypeProvider typeProvider = getMazeTypeProvider();
        Maze maze = generator.generate(HEIGHT, WIDTH, typeProvider);

        // Act
        int edgeCount = countPassableEdges(maze);

        // Assert
        if (isCyclic) {
            assertTrue(edgeCount >= HEIGHT * WIDTH - 1,
                "Цикличный лабиринт должен иметь больше минимального числа рёбер.");
        } else {
            assertEquals(HEIGHT * WIDTH - 1, edgeCount, "Ацикличный лабиринт должен иметь HEIGHT * WIDTH - 1 рёбер.");
        }
    }

    /**
     * Тест на проверку согласованности рёбер лабиринта.
     */
    @Test
    public void testEdgeConsistency() {
        // Arrange
        Generator generator = getGenerator();
        MazeTypeProvider typeProvider = getMazeTypeProvider();
        Maze maze = generator.generate(HEIGHT, WIDTH, typeProvider);

        // Act & Assert
        for (Cell cell : maze.getAllCells()) {
            for (Edge edge : maze.getEdges(cell)) {
                Cell neighbor = edge.to();
                Edge reverseEdge = maze.getEdge(neighbor, cell);
                assertNotNull(reverseEdge, "Обратное ребро должно существовать");
                assertEquals(edge.type(), reverseEdge.type(), "Типы рёбер должны совпадать");
            }
        }
    }

    /**
     * Проверка отсутствия дублирующих рёбер между одной и той же парой ячеек.
     */
    @Test
    public void testNoDuplicateEdgesBetweenCells() {
        // Arrange
        Generator generator = getGenerator();
        MazeTypeProvider typeProvider = getMazeTypeProvider();
        Maze maze = generator.generate(HEIGHT, WIDTH, typeProvider);

        // Act & Assert
        for (Cell cell : maze.getAllCells()) {
            Set<Cell> uniqueNeighbors = new HashSet<>();
            for (Edge edge : maze.getEdges(cell)) {
                Cell neighbor = edge.to();
                assertTrue(uniqueNeighbors.add(neighbor),
                    "Не должно быть дублирующих рёбер между одной и той же парой ячеек.");
            }
        }
    }

    /**
     * Проверка наличия хотя бы одного проходимого рёбра для каждой ячейки (исключая края).
     */
    @Test
    public void testEveryCellHasPassableEdge() {
        // Arrange
        Generator generator = getGenerator();
        MazeTypeProvider typeProvider = getMazeTypeProvider();
        Maze maze = generator.generate(HEIGHT, WIDTH, typeProvider);

        // Act & Assert
        for (Cell cell : maze.getAllCells()) {
            boolean hasPassableEdge = maze.getEdges(cell).stream().anyMatch(edge -> edge.type().isPassable());
            assertTrue(hasPassableEdge, "Каждая ячейка должна иметь хотя бы одно проходимое ребро.");
        }
    }

    /**
     * Тест на невалидные (отрицательные или нулевые) размеры лабиринта.
     */
    @Test
    public void testInvalidDimensions() {
        // Arrange
        Generator generator = getGenerator();
        MazeTypeProvider typeProvider = getMazeTypeProvider();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> generator.generate(0, 10, typeProvider),
            "Ожидается исключение при высоте равной 0");
        assertThrows(IllegalArgumentException.class, () -> generator.generate(10, 0, typeProvider),
            "Ожидается исключение при ширине равной 0");
        assertThrows(IllegalArgumentException.class, () -> generator.generate(-1, 10, typeProvider),
            "Ожидается исключение при отрицательной высоте");
        assertThrows(IllegalArgumentException.class, () -> generator.generate(10, -1, typeProvider),
            "Ожидается исключение при отрицательной ширине");
    }

    /**
     * Тест на граничные значения размеров лабиринта (1x1).
     */
    @Test
    public void testBoundaryDimension() {
        // Arrange
        Generator generator = getGenerator();
        MazeTypeProvider typeProvider = getMazeTypeProvider();

        // Act
        Maze maze = generator.generate(1, 1, typeProvider);

        // Assert
        assertNotNull(maze, "Лабиринт размера 1x1 должен быть успешно создан");
        assertEquals(1, maze.getAllCells().size(), "Лабиринт размера 1x1 должен содержать одну ячейку");
    }

    /**
     * Тест на корректные размеры лабиринта (10x10).
     */
    @Test
    public void testValidDimensions() {
        // Arrange
        Generator generator = getGenerator();
        MazeTypeProvider typeProvider = getMazeTypeProvider();

        // Act
        Maze maze = generator.generate(HEIGHT, WIDTH, typeProvider);

        // Assert
        assertNotNull(maze, "Лабиринт размера 10x10 должен быть успешно создан");
        assertEquals(HEIGHT * WIDTH, maze.getAllCells().size(), "Лабиринт размера 10x10 должен содержать 100 ячеек");
    }

    /**
     * Подсчитывает количество проходимых рёбер в лабиринте.
     *
     * @param maze Лабиринт для подсчёта рёбер.
     * @return Количество проходимых рёбер.
     */
    private int countPassableEdges(Maze maze) {
        int count = 0;
        Set<Edge> countedEdges = new HashSet<>();
        for (Cell cell : maze.getAllCells()) {
            for (Edge edge : maze.getEdges(cell)) {
                if (edge.type().isPassable() && !countedEdges.contains(edge)) {
                    count++;
                    countedEdges.add(edge);
                    countedEdges.add(maze.getEdge(edge.to(), cell)); // также добавить обратное ребро
                }
            }
        }
        return count;
    }

    /**
     * Проверяет, что лабиринт полностью связен.
     *
     * @param maze Лабиринт для проверки связности.
     * @return true, если все ячейки связаны.
     */
    private boolean isMazeConnected(Maze maze) {
        Set<Cell> visited = new HashSet<>();
        Queue<Cell> queue = new LinkedList<>();
        Cell startCell = maze.getAllCells().iterator().next();
        queue.add(startCell);
        visited.add(startCell);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            for (Edge edge : maze.getEdges(current)) {
                if (edge.type().isPassable()) {
                    Cell neighbor = edge.to();
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        return visited.size() == maze.getAllCells().size();
    }

    /**
     * Проверяет, что лабиринт является деревом (ацикличен и связен).
     *
     * @param maze Лабиринт для проверки.
     * @return true, если лабиринт является деревом.
     */
    private boolean isMazeAcyclic(Maze maze) {
        Set<Cell> visited = new HashSet<>();
        Cell startCell = maze.getAllCells().iterator().next();

        return !hasCycle(maze, null, startCell, visited)
               && visited.size() == maze.getAllCells().size();
    }

    /**
     * Проверяет наличие цикла в графе лабиринта.
     *
     * @param maze    Лабиринт для проверки.
     * @param parent  Предыдущая ячейка в обходе (для исключения обратных рёбер).
     * @param current Текущая ячейка.
     * @param visited Множество посещённых ячеек.
     * @return true, если цикл найден.
     */
    private boolean hasCycle(Maze maze, Cell parent, Cell current, Set<Cell> visited) {
        visited.add(current);

        for (Edge edge : maze.getEdges(current)) {
            if (edge.type().isPassable()) {
                Cell neighbor = edge.to();
                if (!neighbor.equals(parent)) {
                    if (visited.contains(neighbor)) {
                        return true;
                    } else {
                        if (hasCycle(maze, current, neighbor, visited)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
