package backend.academy.generator.huntandkill;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.maze.Maze;
import backend.academy.generator.Generator;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.random.RandomGenerator;
import backend.academy.utils.MazeUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * Генератор лабиринтов с использованием алгоритма Охота и Убийство.
 */
@RequiredArgsConstructor
public class HuntAndKillMazeGenerator implements Generator {
    private final RandomGenerator randomGenerator;
    private final MazeUtils mazeUtils;

    @Override
    public Maze generate(int height, int width, MazeTypeProvider typeProvider) {
        Maze maze = new Maze();
        Cell[][] grid = new Cell[height][width];
        boolean[][] visited = new boolean[height][width];

        // Инициализация сетки и стен
        mazeUtils.initializeGridAndWalls(height, width, grid, maze, typeProvider);

        // Старт со случайной ячейки
        int currentRow = randomGenerator.nextInt(height);
        int currentCol = randomGenerator.nextInt(width);
        Cell currentCell = grid[currentRow][currentCol];
        visited[currentRow][currentCol] = true;

        while (true) {
            // Фаза случайного блуждания
            List<Edge> unvisitedNeighbors = mazeUtils.getUnvisitedNeighbors(maze, currentCell, visited);

            if (!unvisitedNeighbors.isEmpty()) {
                // Выбор случайного не посещённого соседа
                Edge edge = unvisitedNeighbors.get(randomGenerator.nextInt(unvisitedNeighbors.size()));

                // Используем утилиту для установки проходного типа и обновления обратного ребра
                mazeUtils.setPassableEdgeAndReverse(maze, edge, typeProvider);

                // Переход к следующей ячейке
                currentCell = edge.to();
                int row = currentCell.coordinate().row();
                int col = currentCell.coordinate().col();
                visited[row][col] = true;
            } else {
                // Фаза охоты
                boolean found = false;
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        if (!visited[row][col]) {
                            Cell cell = grid[row][col];
                            List<Edge> visitedNeighbors = mazeUtils.getVisitedNeighbors(maze, cell, visited);
                            if (!visitedNeighbors.isEmpty()) {
                                // Удаляем стену между ячейкой и её посещённым соседом
                                Edge edge = visitedNeighbors.get(randomGenerator.nextInt(visitedNeighbors.size()));

                                // Используем утилиту для установки проходного типа и обновления обратного ребра
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
