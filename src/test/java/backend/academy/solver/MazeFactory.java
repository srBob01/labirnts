package backend.academy.solver;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.cell.CellType;
import backend.academy.entity.cell.Coordinate;
import backend.academy.entity.edge.EdgeType;
import backend.academy.entity.maze.Maze;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public class MazeFactory {

    // Константы для размера лабиринта
    @Getter
    private final int mazeSize = 5;

    // Координаты начальной и конечной точек
    @Getter
    private final Coordinate startCoord = new Coordinate(0, 0);
    @Getter
    private final Coordinate endCoord = new Coordinate(4, 4);

    // Типы ячеек
    private final CellType passage = CellType.PASSAGE;
    private final CellType badSurface = CellType.BAD_SURFACE;
    private final CellType goodSurface = CellType.GOOD_SURFACE;

    // Типы переходов (рёбер)
    private final EdgeType badTransition = EdgeType.BAD_TRANSITION;
    private final EdgeType goodTransition = EdgeType.GOOD_TRANSITION;
    private final EdgeType transition = EdgeType.TRANSITION;

    // Стоимости для каждого пути
    @Getter
    private final int pathCostMax = 37;   // Путь 1
    @Getter
    private final int pathCostMin = 10;   // Путь 2 (оптимальный)
    @Getter
    private final int pathCostMiddle = 20; // Путь 3

    /**
     * Создаёт лабиринт с тремя различными путями от начальной до конечной точки:
     *
     * <ul>
     *     <li><b>Путь 1</b>: Самый короткий по количеству шагов, но самый дорогой по стоимости.</li>
     *     <li><b>Путь 2</b>: Более длинный по количеству шагов, но дешевле по стоимости (оптимальный).</li>
     *     <li><b>Путь 3</b>: Средний по количеству шагов и стоимости.</li>
     * </ul>
     * <p>
     * Пути разработаны для тестирования способности решателей находить оптимальный путь на основе стоимости.
     *
     * @return Лабиринт с несколькими путями.
     */
    public Maze createMultiplePathsMaze() {
        Maze maze = new Maze();
        Map<Coordinate, Cell> cells = createCells(maze);

        // Путь 1: Короткий по шагам, но дорогой по стоимости
        // Путь: (0,0) → (0,1) → (0,2) → (0,3) → (0,4) → (1,4) → (2,4) → (3,4) → (4,4)
        // Рёбра и ячейки дорогие

        // Обновляем типы ячеек для Пути 1
        for (int col = 1; col <= 4; col++) {
            cells.get(new Coordinate(0, col)).cellType(badSurface);
        }
        for (int row = 1; row <= 3; row++) {
            cells.get(new Coordinate(row, 4)).cellType(badSurface);
        }

        // Добавляем рёбра для Пути 1
        addEdge(maze, cells, new Coordinate(0, 0), new Coordinate(0, 1), badTransition);
        addEdge(maze, cells, new Coordinate(0, 1), new Coordinate(0, 2), badTransition);
        addEdge(maze, cells, new Coordinate(0, 2), new Coordinate(0, 3), badTransition);
        addEdge(maze, cells, new Coordinate(0, 3), new Coordinate(0, 4), badTransition);
        addEdge(maze, cells, new Coordinate(0, 4), new Coordinate(1, 4), badTransition);
        addEdge(maze, cells, new Coordinate(1, 4), new Coordinate(2, 4), badTransition);
        addEdge(maze, cells, new Coordinate(2, 4), new Coordinate(3, 4), badTransition);
        addEdge(maze, cells, new Coordinate(3, 4), new Coordinate(4, 4), badTransition);

        // Путь 2: Длиннее по шагам, но дешевле по стоимости (оптимальный)
        // Путь: (0,0) → (1,0) → (2,0) → (3,0) → (4,0) → (4,1) → (4,2) → (4,3) → (4,4)
        // Рёбра и ячейки дешёвые

        // Обновляем типы ячеек для Пути 2
        for (int row = 1; row <= 4; row++) {
            cells.get(new Coordinate(row, 0)).cellType(goodSurface);
        }
        for (int col = 1; col <= 3; col++) {
            cells.get(new Coordinate(4, col)).cellType(goodSurface);
        }

        // Добавляем рёбра для Пути 2
        addEdge(maze, cells, new Coordinate(0, 0), new Coordinate(1, 0), goodTransition);
        addEdge(maze, cells, new Coordinate(1, 0), new Coordinate(2, 0), goodTransition);
        addEdge(maze, cells, new Coordinate(2, 0), new Coordinate(3, 0), goodTransition);
        addEdge(maze, cells, new Coordinate(3, 0), new Coordinate(4, 0), goodTransition);
        addEdge(maze, cells, new Coordinate(4, 0), new Coordinate(4, 1), goodTransition);
        addEdge(maze, cells, new Coordinate(4, 1), new Coordinate(4, 2), goodTransition);
        addEdge(maze, cells, new Coordinate(4, 2), new Coordinate(4, 3), goodTransition);
        addEdge(maze, cells, new Coordinate(4, 3), new Coordinate(4, 4), goodTransition);

        // Путь 3: Средний по шагам и стоимости
        // Путь: (0,0) → (1,1) → (2,2) → (3,3) → (4,4)
        // Рёбра и ячейки со средней стоимостью

        // Обновляем типы ячеек для Пути 3
        cells.get(new Coordinate(1, 1)).cellType(badSurface);
        cells.get(new Coordinate(2, 2)).cellType(badSurface);
        cells.get(new Coordinate(3, 3)).cellType(badSurface);

        // Добавляем рёбра для Пути 3
        addEdge(maze, cells, new Coordinate(0, 0), new Coordinate(1, 1), transition);
        addEdge(maze, cells, new Coordinate(1, 1), new Coordinate(2, 2), badTransition);
        addEdge(maze, cells, new Coordinate(2, 2), new Coordinate(3, 3), transition);
        addEdge(maze, cells, new Coordinate(3, 3), new Coordinate(4, 4), transition);

        return maze;
    }

    /**
     * Вспомогательный метод для добавления ребра между двумя ячейками.
     */
    private void addEdge(Maze maze, Map<Coordinate, Cell> cells, Coordinate from, Coordinate to, EdgeType edgeType) {
        maze.addEdge(cells.get(from), cells.get(to), edgeType);
    }

    /**
     * Создаёт непроходимый лабиринт, в котором все рёбра имеют тип `WALL`.
     *
     * @return Непроходимый лабиринт.
     */
    public Maze createUnreachableMaze() {
        Maze maze = new Maze();
        Map<Coordinate, Cell> cells = createCells(maze);

        // Добавляем стены между всеми смежными ячейками
        for (int row = 0; row < mazeSize; row++) {
            for (int col = 0; col < mazeSize; col++) {
                Coordinate current = new Coordinate(row, col);
                addWallsAroundCell(maze, cells, current);
            }
        }
        return maze;
    }

    /**
     * Вспомогательный метод для добавления стен вокруг ячейки.
     */
    private void addWallsAroundCell(Maze maze, Map<Coordinate, Cell> cells, Coordinate coord) {
        int row = coord.row();
        int col = coord.col();
        Coordinate[] neighbors = {
            new Coordinate(row - 1, col), // Верхняя ячейка
            new Coordinate(row + 1, col), // Нижняя ячейка
            new Coordinate(row, col - 1), // Левая ячейка
            new Coordinate(row, col + 1)  // Правая ячейка
        };

        Cell cell = cells.get(coord);
        for (Coordinate neighbor : neighbors) {
            if (cells.containsKey(neighbor)) {
                Cell neighborCell = cells.get(neighbor);
                maze.addEdge(cell, neighborCell, EdgeType.WALL); // Добавляем стену между ячейками
            }
        }
    }

    /**
     * Создаёт ячейки для лабиринта.
     */
    private Map<Coordinate, Cell> createCells(Maze maze) {
        Map<Coordinate, Cell> cells = new HashMap<>();
        for (int row = 0; row < mazeSize; row++) {
            for (int col = 0; col < mazeSize; col++) {
                Coordinate coord = new Coordinate(row, col);
                Cell cell = new Cell(row, col, passage);
                maze.addCell(cell);
                cells.put(coord, cell);
            }
        }
        return cells;
    }
}
