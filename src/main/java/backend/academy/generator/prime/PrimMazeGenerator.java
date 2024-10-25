package backend.academy.generator.prime;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.maze.Maze;
import backend.academy.generator.Generator;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.random.RandomGenerator;
import backend.academy.utils.MazeUtils;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;

/**
 * Генератор лабиринтов с использованием алгоритма Прима, начиная с одной случайной ячейки и постепенно
 * добавляя соседние рёбра к лабиринту, пока не будут соединены все ячейки.
 */
@RequiredArgsConstructor
public class PrimMazeGenerator implements Generator {

    private static final Logger LOGGER = Logger.getLogger(PrimMazeGenerator.class.getName());

    private final RandomGenerator randomGenerator;
    private final MazeUtils mazeUtils;

    @Override
    public Maze generate(int height, int width, MazeTypeProvider typeProvider) {
        if (height < 1 || width < 1) {
            LOGGER.warning("The dimensions of the maze must be positive.");
            throw new IllegalArgumentException("The dimensions of the maze must be positive");
        }

        Cell[][] grid = new Cell[height][width];
        Maze maze = new Maze();

        mazeUtils.initializeGridAndWalls(height, width, grid, maze, typeProvider);

        int startRow = randomGenerator.nextInt(height);
        int startCol = randomGenerator.nextInt(width);
        Cell startCell = grid[startRow][startCol];

        // Помечаем ячейки как не входящие в лабиринт
        boolean[][] inMaze = new boolean[height][width];
        inMaze[startRow][startCol] = true;

        // Граничные рёбра стартовой ячейки
        Set<Edge> frontierEdges = new HashSet<>(maze.getEdges(startCell));

        while (!frontierEdges.isEmpty()) {
            // Преобразуем Set в список для выбора случайного ребра
            Edge[] edgesArray = frontierEdges.toArray(new Edge[0]);
            int index = randomGenerator.nextInt(edgesArray.length);
            Edge edge = edgesArray[index];

            // Удаляем выбранное ребро из множества пограничных
            frontierEdges.remove(edge);

            // Получаем соседа через выбранное ребро
            Cell neighbor = edge.to();
            int nRow = neighbor.coordinate().row();
            int nCol = neighbor.coordinate().col();

            // Если сосед ещё не в лабиринте
            if (!inMaze[nRow][nCol]) {
                // Устанавливаем ребро как проходимое и добавляем ячейку в лабиринт
                mazeUtils.setPassableEdgeAndReverse(maze, edge, typeProvider);

                inMaze[nRow][nCol] = true;

                // Добавляем рёбра нового соседа к пограничным
                frontierEdges.addAll(mazeUtils.getUnvisitedNeighbors(maze, neighbor, inMaze));
            }
        }
        return maze;
    }
}
