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
import lombok.RequiredArgsConstructor;

/**
 * Генератор лабиринтов с использованием алгоритма Прима.
 */
@RequiredArgsConstructor
public class PrimMazeGenerator implements Generator {
    private final RandomGenerator randomGenerator;
    private final MazeUtils mazeUtils;

    @Override
    public Maze generate(int height, int width, MazeTypeProvider typeProvider) {
        Cell[][] grid = new Cell[height][width];
        Maze maze = new Maze();

        // Инициализация сетки и стен
        mazeUtils.initializeGridAndWalls(height, width, grid, maze, typeProvider);

        // Выбираем случайную начальную ячейку
        int startRow = randomGenerator.nextInt(height);
        int startCol = randomGenerator.nextInt(width);
        Cell startCell = grid[startRow][startCol];

        // Помечаем ячейки как не входящие в лабиринт
        boolean[][] inMaze = new boolean[height][width];
        inMaze[startRow][startCol] = true;

        // Используем Set для хранения рёбер на границе, чтобы избежать дубликатов
        Set<Edge> frontierEdges = new HashSet<>(maze.getEdges(startCell));

        // Основной цикл алгоритма
        while (!frontierEdges.isEmpty()) {
            // Преобразуем Set в список для выбора случайного ребра
            Edge[] edgesArray = frontierEdges.toArray(new Edge[0]);
            int index = randomGenerator.nextInt(edgesArray.length);
            Edge edge = edgesArray[index];
            frontierEdges.remove(edge);

            Cell neighbor = edge.to();
            int nRow = neighbor.coordinate().row();
            int nCol = neighbor.coordinate().col();

            if (!inMaze[nRow][nCol]) {
                // Меняем тип ребра на проходной
                edge.type(typeProvider.getPassableEdgeType());
                // Также обновляем обратное ребро
                Edge reverseEdge = maze.getEdge(neighbor, edge.from());
                if (reverseEdge != null) {
                    reverseEdge.type(typeProvider.getPassableEdgeType());
                }

                // Помечаем соседнюю ячейку как входящую в лабиринт
                inMaze[nRow][nCol] = true;

                // Получаем все рёбра соседней ячейки и добавляем непосещённые в границу
                frontierEdges.addAll(mazeUtils.getUnvisitedNeighbors(maze, neighbor, inMaze));
            }
        }
        return maze;
    }
}
