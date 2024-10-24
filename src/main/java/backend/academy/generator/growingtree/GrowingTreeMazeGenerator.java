package backend.academy.generator.growingtree;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.maze.Maze;
import backend.academy.generator.Generator;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.random.RandomGenerator;
import backend.academy.utils.MazeUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * Генератор лабиринтов с использованием алгоритма Growing Tree.
 */
@AllArgsConstructor
public class GrowingTreeMazeGenerator implements Generator {
    private final RandomGenerator randomGenerator;
    private final MazeUtils mazeUtils;
    @Setter
    private SelectionStrategyGrowingTree selectionStrategy;

    @Override
    public Maze generate(int height, int width, MazeTypeProvider typeProvider) {
        Maze maze = new Maze();
        Cell[][] grid = new Cell[height][width];
        boolean[][] visited = new boolean[height][width];

        // Инициализация сетки и стен
        mazeUtils.initializeGridAndWalls(height, width, grid, maze, typeProvider);

        List<Cell> activeCells = new ArrayList<>();

        // Выбор случайной начальной ячейки
        int startRow = randomGenerator.nextInt(height);
        int startCol = randomGenerator.nextInt(width);
        Cell startCell = grid[startRow][startCol];

        visited[startRow][startCol] = true;
        activeCells.add(startCell);

        // Основной цикл алгоритма
        while (!activeCells.isEmpty()) {
            Cell currentCell = selectActiveCell(activeCells, selectionStrategy);

            // Получаем список непосещённых соседей
            List<Edge> unvisitedNeighbors = mazeUtils.getUnvisitedNeighbors(maze, currentCell, visited);

            if (!unvisitedNeighbors.isEmpty()) {
                // Выбор случайного непосещённого соседа
                Edge edge = unvisitedNeighbors.get(randomGenerator.nextInt(unvisitedNeighbors.size()));

                // Используем утилиту для установки проходного типа и обновления обратного ребра
                mazeUtils.setPassableEdgeAndReverse(maze, edge, typeProvider);

                // Помечаем соседа как посещённого и добавляем его в активные ячейки
                Cell neighborCell = edge.to();
                visited[neighborCell.coordinate().row()][neighborCell.coordinate().col()] = true;
                activeCells.add(neighborCell);
            } else {
                // Убираем текущую ячейку из активных ячеек
                activeCells.remove(currentCell);
            }
        }

        return maze;
    }

    /**
     * Выбирает активную ячейку на основе выбранной стратегии.
     *
     * @param activeCells Список активных ячеек.
     * @return Выбранная активная ячейка.
     */
    private Cell selectActiveCell(List<Cell> activeCells, SelectionStrategyGrowingTree selectionStrategy) {
        return switch (selectionStrategy) {
            case RANDOM -> activeCells.get(randomGenerator.nextInt(activeCells.size()));
            case LAST -> activeCells.getLast();
            case FIRST -> activeCells.getFirst();
        };
    }
}
