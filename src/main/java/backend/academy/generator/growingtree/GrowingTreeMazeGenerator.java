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
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * Генератор лабиринтов с использованием алгоритма Growing Tree.
 * Алгоритм "растущего дерева" заключается в добавлении ячеек в лабиринт по мере их посещения,
 * пока все ячейки не будут включены в лабиринт.
 * Стратегия выбора активной ячейки определяется через SelectionStrategyGrowingTree.
 */
@AllArgsConstructor
public class GrowingTreeMazeGenerator implements Generator {

    private static final Logger LOGGER = Logger.getLogger(GrowingTreeMazeGenerator.class.getName());

    private final RandomGenerator randomGenerator;
    private final MazeUtils mazeUtils;
    @Setter
    private SelectionStrategyGrowingTree selectionStrategy;

    @Override
    public Maze generate(int height, int width, MazeTypeProvider typeProvider) {
        if (height < 1 || width < 1) {
            LOGGER.warning("The dimensions of the maze must be positive.");
            throw new IllegalArgumentException("The dimensions of the maze must be positive");
        }
        Maze maze = new Maze();
        Cell[][] grid = new Cell[height][width];
        boolean[][] visited = new boolean[height][width];

        mazeUtils.initializeGridAndWalls(height, width, grid, maze, typeProvider);

        List<Cell> activeCells = new ArrayList<>();

        int startRow = randomGenerator.nextInt(height);
        int startCol = randomGenerator.nextInt(width);
        Cell startCell = grid[startRow][startCol];

        // Помечаем стартовую ячейку как посещённую
        visited[startRow][startCol] = true;
        activeCells.add(startCell);

        // Пока есть активные ячейки
        while (!activeCells.isEmpty()) {
            Cell currentCell = selectActiveCell(activeCells, selectionStrategy);

            // Получаем список не посещённых соседей
            List<Edge> unvisitedNeighbors = mazeUtils.getUnvisitedNeighbors(maze, currentCell, visited);

            if (!unvisitedNeighbors.isEmpty()) {
                // Если есть не посещённые соседи, выбираем случайного соседа
                Edge edge = unvisitedNeighbors.get(randomGenerator.nextInt(unvisitedNeighbors.size()));

                // Устанавливаем проход между текущей ячейкой и выбранным соседом
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
