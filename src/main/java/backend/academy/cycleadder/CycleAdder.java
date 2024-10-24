package backend.academy.cycleadder;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.maze.Maze;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.random.RandomGenerator;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * Класс для добавления циклов в существующий лабиринт на основе вероятности.
 */
@RequiredArgsConstructor
public class CycleAdder {
    // Максимальный процент для вероятности
    private static final int MAX_PERCENTAGE = 50;

    private final RandomGenerator randomGenerator;

    /**
     * Добавляет циклы в лабиринт путем случайного удаления стен.
     *
     * @param maze         Лабиринт для модификации.
     * @param typeProvider Поставщик типов для ячеек и рёбер лабиринта.
     */
    public void addCycles(Maze maze, MazeTypeProvider typeProvider, CycleLevelType cycleLevelType) {
        List<Edge> potentialEdgesForCycles = new ArrayList<>();

        // Сбор всех стен, которые могут быть удалены для создания циклов
        for (Cell cell : maze.getAllCells()) {
            for (Edge edge : maze.getEdges(cell)) {
                if (!typeProvider.isPassage(edge.type())) {
                    // Добавляем только одно направление рёбер
                    if (cell.coordinate().compareTo(edge.to().coordinate()) < 0) {
                        potentialEdgesForCycles.add(edge);
                    }
                }
            }
        }

        // Для каждого потенциального ребра, решаем, удалять ли стену
        for (Edge edge : potentialEdgesForCycles) {
            if (randomGenerator.nextInt(MAX_PERCENTAGE) < cycleLevelType.cycleProbability()) {
                // Меняем тип ребра на проходной
                edge.type(typeProvider.getPassableEdgeType());

                // Также обновляем обратное ребро
                Edge reverseEdge = maze.getEdge(edge.to(), edge.from());
                if (reverseEdge != null) {
                    reverseEdge.type(typeProvider.getPassableEdgeType());
                }
            }
        }
    }
}
