package backend.academy.generator.kruskal;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.maze.Maze;
import backend.academy.generator.Generator;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.random.RandomGenerator;
import backend.academy.utils.MazeUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;

/**
 * Генератор лабиринтов с использованием алгоритма Крускала.
 */
@RequiredArgsConstructor
public class KruskalMazeGenerator implements Generator {
    private final RandomGenerator randomGenerator;
    private final MazeUtils mazeUtils;

    @Override
    public Maze generate(int height, int width, MazeTypeProvider typeProvider) {
        Maze maze = new Maze();
        Cell[][] grid = new Cell[height][width];

        // Инициализация сетки и стен
        mazeUtils.initializeGridAndWalls(height, width, grid, maze, typeProvider);

        // Инициализация DisjointSet структуры (непересекающиеся множества)
        DisjointSet<Cell> disjointSet = new DisjointSet<>();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                disjointSet.makeSet(cell); // Создаём множество для каждой ячейки
            }
        }

        // Получаем список всех рёбер (стен) из лабиринта
        Set<Edge> edges = new HashSet<>();
        for (Cell cell : maze.getAllCells()) {
            for (Edge edge : maze.getEdges(cell)) {
                // Добавляем только один экземпляр ребра (если from <= to по некоторому критерию)
                if (cell.coordinate().row() <= edge.to().coordinate().row() &&
                    cell.coordinate().col() <= edge.to().coordinate().col()) {
                    edges.add(edge);
                }
            }
        }

        // Преобразуем Set в список для перемешивания
        List<Edge> edgeList = new ArrayList<>(edges);

        // Перемешиваем список рёбер случайным образом
        Collections.shuffle(edgeList, new Random() {
            @Override
            public int nextInt(int bound) {
                return randomGenerator.nextInt(bound);
            }
        });

        // Алгоритм Крускала для генерации лабиринта
        for (Edge edge : edgeList) {
            Cell cell1 = edge.from();
            Cell cell2 = edge.to();

            Cell parent1 = disjointSet.findSet(cell1);
            Cell parent2 = disjointSet.findSet(cell2);

            if (!parent1.equals(parent2)) {
                // Объединяем множества
                disjointSet.union(cell1, cell2);

                // Меняем тип ребра на проходной
                edge.type(typeProvider.getPassableEdgeType());
                // Также обновляем обратное ребро
                Edge reverseEdge = maze.getEdge(cell2, cell1);
                if (reverseEdge != null) {
                    reverseEdge.type(typeProvider.getPassableEdgeType());
                }
            }
        }

        return maze;
    }
}
