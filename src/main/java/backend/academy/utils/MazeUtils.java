package backend.academy.utils;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.direction.Direction;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.maze.Maze;
import backend.academy.mazetype.MazeTypeProvider;
import java.util.ArrayList;
import java.util.List;

public class MazeUtils {

    /**
     * Инициализирует сетку ячеек и добавляет их в лабиринт.
     *
     * @param height       Высота лабиринта.
     * @param width        Ширина лабиринта.
     * @param grid         Двумерный массив для хранения ячеек.
     * @param maze         Лабиринт.
     * @param typeProvider Провайдер типов для ячеек.
     */
    public void initializeGrid(int height, int width, Cell[][] grid, Maze maze, MazeTypeProvider typeProvider) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = new Cell(row, col, typeProvider.getCellType());
                grid[row][col] = cell;
                maze.addCell(cell);
            }
        }
    }

    /**
     * Инициализирует сетку ячеек и устанавливает все рёбра между соседними ячейками как стены.
     *
     * @param height       Высота лабиринта.
     * @param width        Ширина лабиринта.
     * @param grid         Сетка ячеек.
     * @param maze         Лабиринт.
     * @param typeProvider Провайдер типов лабиринта.
     */
    public void initializeGridAndWalls(
        int height, int width, Cell[][] grid, Maze maze, MazeTypeProvider typeProvider
    ) {
        initializeGrid(height, width, grid, maze, typeProvider);

        // Установка всех возможных рёбер как стен
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = grid[row][col];

                for (Direction direction : Direction.values()) {
                    int nRow = row + direction.rowOffset();
                    int nCol = col + direction.colOffset();

                    if (nRow >= 0 && nRow < height && nCol >= 0 && nCol < width) {
                        Cell neighbor = grid[nRow][nCol];
                        maze.addOrUpdateEdge(cell, neighbor, typeProvider.getUnPassableEdgeType());
                    }
                }
            }
        }
    }

    /**
     * Общий метод для получения соседей ячейки, фильтруя их по состоянию посещения.
     *
     * @param maze      Лабиринт.
     * @param current   Текущая ячейка.
     * @param visited   Массив посещённых ячеек.
     * @param isVisited Функция, определяющая, был ли сосед посещён или нет.
     * @return Список соседей, отфильтрованных по состоянию посещения.
     */
    private List<Edge> getFilteredNeighbors(Maze maze, Cell current, boolean[][] visited, boolean isVisited) {
        List<Edge> neighbors = new ArrayList<>();
        for (Edge edge : maze.getEdges(current)) {
            Cell neighbor = edge.to();
            int nRow = neighbor.coordinate().row();
            int nCol = neighbor.coordinate().col();
            if (visited[nRow][nCol] == isVisited) {
                neighbors.add(edge);
            }
        }
        return neighbors;
    }

    /**
     * Возвращает список рёбер, ведущих к непосещённым соседям.
     *
     * @param maze    Лабиринт.
     * @param current Текущая ячейка.
     * @param visited Массив посещённых ячеек.
     * @return Список рёбер, ведущих к непосещённым соседям.
     */
    public List<Edge> getUnvisitedNeighbors(Maze maze, Cell current, boolean[][] visited) {
        return getFilteredNeighbors(maze, current, visited, false);
    }

    /**
     * Возвращает список рёбер к посещённым соседям текущей ячейки.
     *
     * @param maze    Лабиринт.
     * @param current Текущая ячейка.
     * @param visited Массив посещённых ячеек.
     * @return Список рёбер к посещённым соседям.
     */
    public List<Edge> getVisitedNeighbors(Maze maze, Cell current, boolean[][] visited) {
        return getFilteredNeighbors(maze, current, visited, true);
    }

    /**
     * Устанавливает тип ребра как проходной и обновляет обратное ребро.
     *
     * @param maze         Лабиринт.
     * @param edge         Ребро, тип которого нужно изменить.
     * @param typeProvider Провайдер типов для лабиринта.
     */
    public void setPassableEdgeAndReverse(Maze maze, Edge edge, MazeTypeProvider typeProvider) {
        Cell currentCell = edge.from();
        Cell neighborCell = edge.to();

        // Меняем тип ребра на проходной
        edge.type(typeProvider.getPassableEdgeType());

        // Также обновляем обратное ребро
        Edge reverseEdge = maze.getEdge(neighborCell, currentCell);
        if (reverseEdge != null) {
            reverseEdge.type(typeProvider.getPassableEdgeType());
        }
    }

    /**
     * Устанавливает тип ребра между двумя ячейками как непроходимое (стена),
     * а также обновляет обратное ребро, если оно существует.
     *
     * @param maze         Лабиринт, в котором производится изменение рёбер.
     * @param from         Ячейка, от которой исходит ребро.
     * @param to           Ячейка, к которой ведёт ребро.
     * @param typeProvider Провайдер типов рёбер, который предоставляет тип непроходимого ребра.
     */
    public void setUnpassableEdgeAndReverse(Maze maze, Cell from, Cell to, MazeTypeProvider typeProvider) {
        // Получаем ребро между двумя ячейками
        Edge edge = maze.getEdge(from, to);

        // Если ребро существует, меняем его тип на непроходимый
        if (edge != null) {
            edge.type(typeProvider.getUnPassableEdgeType());

            // Находим и обновляем обратное ребро (из ячейки "to" в ячейку "from"), если оно существует
            Edge reverseEdge = maze.getEdge(to, from);
            if (reverseEdge != null) {
                reverseEdge.type(typeProvider.getUnPassableEdgeType());
            }
        }
    }

}
