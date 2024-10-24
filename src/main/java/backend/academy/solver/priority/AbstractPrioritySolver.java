package backend.academy.solver.priority;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.cell.Coordinate;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.maze.Maze;
import backend.academy.entity.path.Path;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.solver.Solver;
import backend.academy.utils.ReconstructorPath;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import lombok.RequiredArgsConstructor;

/**
 * Абстрактный класс для реализации общих частей алгоритмов поиска (Дейкстра и A*).
 */
@RequiredArgsConstructor
public abstract class AbstractPrioritySolver implements Solver {
    private static final float LOAD_FACTOR = 0.75f;

    private final ReconstructorPath reconstructorPath;

    @Override
    public Path solve(Maze maze, Coordinate startCoord, Coordinate endCoord, MazeTypeProvider mazeTypeProvider) {
        int expectedSize = maze.getAllCells().size();

        Map<Cell, Cell> predecessors = new HashMap<>((int) (expectedSize / LOAD_FACTOR) + 1);
        Map<Cell, Integer> gScores = new HashMap<>((int) (expectedSize / LOAD_FACTOR) + 1);
        Set<Cell> closedSet = new HashSet<>((int) (expectedSize / LOAD_FACTOR) + 1);
        PriorityQueue<CellScore> openSet = new PriorityQueue<>(Comparator.comparingInt(cs -> cs.priority));

        Cell startCell = maze.getCell(startCoord);
        Cell endCell = maze.getCell(endCoord);

        gScores.put(startCell, startCell.cellType().movementCost());
        openSet.add(
            new CellScore(startCell, calculatePriority(startCell, endCell, startCell.cellType().movementCost())));

        while (!openSet.isEmpty()) {
            CellScore cs = openSet.poll();
            Cell current = cs.cell;

            if (closedSet.contains(current)) {
                continue;
            }
            closedSet.add(current);

            if (current.equals(endCell)) {
                return reconstructorPath.reconstruct(predecessors, endCell, gScores.get(endCell));
            }

            for (Edge edge : maze.getEdges(current)) {
                if (mazeTypeProvider.isPassage(edge.type())) {
                    Cell neighbor = edge.to();
                    if (closedSet.contains(neighbor)) {
                        continue;
                    }

                    int weight = edge.type().movementCost() + neighbor.cellType().movementCost();
                    int tentativeGScore = gScores.get(current) + weight;

                    Integer neighborGScore = gScores.get(neighbor);
                    if (neighborGScore == null || tentativeGScore < neighborGScore) {
                        gScores.put(neighbor, tentativeGScore);
                        predecessors.put(neighbor, current);
                        int fScore = calculatePriority(neighbor, endCell, tentativeGScore);
                        openSet.add(new CellScore(neighbor, fScore));
                    }
                }
            }
        }

        return new Path(Collections.emptyList(), 0);
    }

    /**
     * Абстрактный метод для вычисления приоритета ячейки.
     *
     * @param cell    Текущая ячейка.
     * @param endCell Конечная ячейка.
     * @param gScore  Стоимость пути до текущей ячейки.
     * @return Приоритет для очереди.
     */
    protected abstract int calculatePriority(Cell cell, Cell endCell, int gScore);

    /**
     * Вспомогательный класс для хранения ячейки и её приоритета.
     */
    private record CellScore(Cell cell, int priority) {
    }
}
