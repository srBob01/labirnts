package backend.academy.solver.fs;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.cell.Coordinate;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.maze.Maze;
import backend.academy.entity.path.Path;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.solver.Solver;
import backend.academy.utils.ReconstructorPath;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;

/**
 * Абстрактный класс, который объединяет общие части для DFS и BFS с учётом весов ячеек и рёбер.
 */
@RequiredArgsConstructor
public abstract class AbstractSolver implements Solver {
    private final ReconstructorPath reconstructorPath;

    /**
     * Основной метод для поиска пути, который реализует общую логику для DFS и BFS.
     *
     * @param maze             Лабиринт.
     * @param startCoord       Начальные координаты.
     * @param endCoord         Конечные координаты.
     * @param mazeTypeProvider Поставщик типов для лабиринта.
     * @return Путь.
     */
    @Override
    public Path solve(Maze maze, Coordinate startCoord, Coordinate endCoord, MazeTypeProvider mazeTypeProvider) {
        clearStructure();
        Map<Cell, Cell> predecessors = new HashMap<>();
        Set<Cell> visited = new HashSet<>();
        Map<Cell, Integer> costMap = new HashMap<>();

        Cell startCell = maze.getCell(startCoord);
        Cell endCell = maze.getCell(endCoord);

        if (startCell == null || endCell == null) {
            return new Path(Collections.emptyList(), 0);
        }

        if (startCell.equals(endCell)) {
            return new Path(Collections.singletonList(startCoord), startCell.cellType().movementCost());
        }

        // Добавляем начальную ячейку в структуру и отмечаем её как посещённую
        addToStructure(startCell);
        visited.add(startCell);
        costMap.put(startCell, startCell.cellType().movementCost());

        while (!isStructureEmpty()) {
            Cell current = retrieveFromStructure();

            // Если нашли конечную ячейку, восстанавливаем путь
            if (current.equals(endCell)) {
                int totalCost = costMap.get(endCell);
                return reconstructorPath.reconstruct(predecessors, endCell, totalCost);
            }

            // Обработка всех соседей текущей ячейки
            for (Edge edge : maze.getEdges(current)) {
                if (mazeTypeProvider.isPassage(edge.type())) {
                    Cell neighbor = edge.to();
                    int currentCost = costMap.get(current);
                    int newCost = currentCost + edge.type().movementCost() + neighbor.cellType().movementCost();

                    // Добавляем соседа в структуру, если он ещё не посещён
                    if (visited.add(neighbor)) {
                        addToStructure(neighbor);
                        predecessors.put(neighbor, current);
                        costMap.put(neighbor, newCost);
                    }
                }
            }
        }

        return new Path(Collections.emptyList(), 0);
    }

    /**
     * Добавляет ячейку в структуру данных.
     *
     * @param cell Ячейка для добавления.
     */
    protected abstract void addToStructure(Cell cell);

    /**
     * Извлекает ячейку из структуры данных.
     *
     * @return Извлечённая ячейка.
     */
    protected abstract Cell retrieveFromStructure();

    /**
     * Проверяет, пуста ли структура данных.
     *
     * @return true, если структура пуста, иначе false.
     */
    protected abstract boolean isStructureEmpty();

    /**
     * Очищает используемую структуру данных, чтобы удалить все ранее добавленные элементы.
     */
    protected abstract void clearStructure();
}
