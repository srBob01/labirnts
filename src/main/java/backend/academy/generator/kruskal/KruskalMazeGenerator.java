package backend.academy.generator.kruskal;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.maze.Maze;
import backend.academy.generator.Generator;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.utils.MazeUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;

/**
 * Генератор лабиринтов с использованием алгоритма Крускала, путём объединения ячеек с помощью рёбер,
 * выбранных случайным образом, и исключения циклов. Он работает, создавая минимальное остовное дерево
 * в графе ячеек и рёбер, что гарантирует, что все ячейки будут соединены.
 */
@RequiredArgsConstructor
public class KruskalMazeGenerator implements Generator {

    private static final Logger LOGGER = Logger.getLogger(KruskalMazeGenerator.class.getName());

    private final MazeUtils mazeUtils;

    @Override
    public Maze generate(int height, int width, MazeTypeProvider typeProvider) {
        if (height < 1 || width < 1) {
            LOGGER.warning("The dimensions of the maze must be positive.");
            throw new IllegalArgumentException("The dimensions of the maze must be positive");
        }

        Maze maze = new Maze();
        Cell[][] grid = new Cell[height][width];

        mazeUtils.initializeGridAndWalls(height, width, grid, maze, typeProvider);

        // Создание раздельных множеств (Disjoint Set) для каждой ячейки
        DisjointSet<Cell> disjointSet = new DisjointSet<>();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                disjointSet.makeSet(cell);
            }
        }

        Set<Edge> edges = new HashSet<>();
        for (Cell cell : maze.getAllCells()) {
            for (Edge edge : maze.getEdges(cell)) {
                // Добавляем только один экземпляр ребра
                if (cell.coordinate().row() <= edge.to().coordinate().row()
                    && cell.coordinate().col() <= edge.to().coordinate().col()) {
                    edges.add(edge);
                }
            }
        }

        // Преобразуем Set в список для перемешивания
        List<Edge> edgeList = new ArrayList<>(edges);

        Collections.shuffle(edgeList);

        for (Edge edge : edgeList) {
            Cell cell1 = edge.from();
            Cell cell2 = edge.to();

            // Находим множества, к которым принадлежат ячейки
            Cell parent1 = disjointSet.findSet(cell1);
            Cell parent2 = disjointSet.findSet(cell2);

            // Если ячейки принадлежат разным множествам, соединяем их
            if (!parent1.equals(parent2)) {
                disjointSet.union(cell1, cell2);

                mazeUtils.setPassableEdgeAndReverse(maze, edge, typeProvider);
            }
        }

        return maze;
    }
}
