package backend.academy.generator.huntandkill;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.maze.Maze;
import backend.academy.generator.Generator;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.random.RandomGenerator;
import backend.academy.utils.MazeUtils;
import java.util.List;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;

/**
 * Генератор лабиринтов с использованием алгоритма "Охота и Убийство".
 * Алгоритм состоит из двух фаз: "убийство" (случайное блуждание) и "охота"
 * (поиск не посещённых ячеек с посещёнными соседями).
 * Этот подход гарантирует, что лабиринт будет сгенерирован с минимальным количеством циклов.
 */
@RequiredArgsConstructor
public class HuntAndKillMazeGenerator implements Generator {

    private static final Logger LOGGER = Logger.getLogger(HuntAndKillMazeGenerator.class.getName());

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
        boolean[][] visited = new boolean[height][width];

        mazeUtils.initializeGridAndWalls(height, width, grid, maze, typeProvider);

        int currentRow = randomGenerator.nextInt(height);
        int currentCol = randomGenerator.nextInt(width);
        Cell currentCell = grid[currentRow][currentCol];
        // Помечаем стартовую ячейку как посещённую
        visited[currentRow][currentCol] = true;

        while (true) {
            // Фаза случайного блуждания (убийство)
            List<Edge> unvisitedNeighbors = mazeUtils.getUnvisitedNeighbors(maze, currentCell, visited);

            if (!unvisitedNeighbors.isEmpty()) {
                // Если есть не посещённые соседи, выбираем случайного соседа
                Edge edge = unvisitedNeighbors.get(randomGenerator.nextInt(unvisitedNeighbors.size()));

                // Устанавливаем проход между текущей ячейкой и выбранным соседом
                mazeUtils.setPassableEdgeAndReverse(maze, edge, typeProvider);

                // Переход к следующей ячейке
                currentCell = edge.to();
                int row = currentCell.coordinate().row();
                int col = currentCell.coordinate().col();

                // Помечаем её как посещённую
                visited[row][col] = true;
            } else {
                // Фаза охоты
                boolean found = false;

                // Поиск не посещённой ячейки с посещёнными соседями
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        if (!visited[row][col]) {
                            Cell cell = grid[row][col];
                            // Получаем список рёбер к посещённым соседям текущей ячейки
                            List<Edge> visitedNeighbors = mazeUtils.getVisitedNeighbors(maze, cell, visited);
                            if (!visitedNeighbors.isEmpty()) {
                                // И если он не пуст, удаляем стену между ячейкой и её случайным посещённым соседом
                                Edge edge = visitedNeighbors.get(randomGenerator.nextInt(visitedNeighbors.size()));

                                mazeUtils.setPassableEdgeAndReverse(maze, edge, typeProvider);

                                // Помечаем ячейку как посещённую и переходим к ней
                                visited[row][col] = true;
                                currentCell = cell;
                                found = true;
                                break;
                            }
                        }
                    }
                    if (found) {
                        // Если ячейка найдена, возвращаемся к фазе блуждания
                        break;
                    }
                }
                // Если больше нет не посещённых ячеек, завершаем генерацию
                if (!found) {
                    break;
                }
            }
        }
        return maze;
    }
}
