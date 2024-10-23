package backend.academy.entity.maze;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.cell.Coordinate;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.edge.EdgeType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Класс Maze представляет лабиринт, построенный в виде графа, где каждая ячейка соединена ребрами с соседними
 * ячейками. Лабиринт хранится как список смежности (adjacencyList), где каждая ячейка содержит список рёбер,
 * указывающих на соседние ячейки.
 */

public class Maze {
    // Карта, где ключом является ячейка, а значением — список рёбер, ведущих к соседним ячейкам.
    private final Map<Cell, List<Edge>> adjacencyEdgeMap;

    public Maze() {
        adjacencyEdgeMap = new HashMap<>();
    }

    /**
     * Добавляет ячейку в лабиринт.
     *
     * @param cell Ячейка для добавления.
     */
    public void addCell(Cell cell) {
        adjacencyEdgeMap.putIfAbsent(cell, new ArrayList<>());
    }

    /**
     * Добавляет ребро между двумя ячейками с указанным типом ребра.
     * Если ребро уже существует, обновляет его тип.
     *
     * @param from     Начальная ячейка.
     * @param to       Конечная ячейка.
     * @param edgeType Тип ребра.
     */
    public void addOrUpdateEdge(Cell from, Cell to, EdgeType edgeType) {
        Edge existingEdge = getEdge(from, to);
        if (existingEdge != null) {
            existingEdge.type(edgeType);
        } else {
            addEdge(from, to, edgeType);
        }
    }

    /**
     * Получает ребро между двумя ячейками, если оно существует.
     *
     * @param from Начальная ячейка.
     * @param to   Конечная ячейка.
     * @return Ребро или null, если оно не существует.
     */
    public Edge getEdge(Cell from, Cell to) {
        List<Edge> edges = adjacencyEdgeMap.get(from);
        if (edges != null) {
            for (Edge edge : edges) {
                if (edge.to().equals(to)) {
                    return edge;
                }
            }
        }
        return null;
    }

    /**
     * Получает ячейку по её координатам.
     *
     * @param coordinate Координаты ячейки.
     * @return Ячейка по данным координатам, или null, если ячейка не найдена.
     */
    public Cell getCell(Coordinate coordinate) {
        return adjacencyEdgeMap.keySet().stream()
            .filter(cell -> cell.coordinate().equals(coordinate))
            .findFirst()
            .orElse(null);
    }

    /**
     * Добавляет ребро между двумя ячейками с указанным типом ребра.
     *
     * @param from     Начальная ячейка.
     * @param to       Конечная ячейка.
     * @param edgeType Тип ребра.
     */
    public void addEdge(Cell from, Cell to, EdgeType edgeType) {
        Edge edge = new Edge(from, to, edgeType);
        adjacencyEdgeMap.computeIfAbsent(from, i -> new ArrayList<>()).add(edge);
        Edge reverseEdge = new Edge(to, from, edgeType);
        adjacencyEdgeMap.computeIfAbsent(to, i -> new ArrayList<>()).add(reverseEdge);
    }

    /**
     * Удаляет ребро между двумя ячейками.
     *
     * @param from Начальная ячейка.
     * @param to   Конечная ячейка.
     */
    public void removeEdge(Cell from, Cell to) {
        List<Edge> edgesFrom = adjacencyEdgeMap.get(from);
        if (edgesFrom != null) {
            edgesFrom.removeIf(edge -> edge.to().equals(to));
        }

        List<Edge> edgesTo = adjacencyEdgeMap.get(to);
        if (edgesTo != null) {
            edgesTo.removeIf(edge -> edge.to().equals(from));
        }
    }

    /**
     * Получает список соседних ячеек для заданной ячейки.
     *
     * @param cell Ячейка, для которой ищутся соседи.
     * @return Список соседних ячеек.
     */
    public List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        List<Edge> edges = adjacencyEdgeMap.get(cell);
        if (edges != null) {
            for (Edge edge : edges) {
                if (edge.type().isPassable()) {
                    neighbors.add(edge.to());
                }
            }
        }
        return neighbors;
    }

    /**
     * Проверяет, содержит ли лабиринт заданную ячейку.
     *
     * @param cell Ячейка для проверки.
     * @return true, если лабиринт содержит ячейку, иначе false.
     */
    public boolean containsCell(Cell cell) {
        return adjacencyEdgeMap.containsKey(cell);
    }

    /**
     * Получает все ячейки в лабиринте.
     *
     * @return Набор всех ячеек.
     */
    public Set<Cell> getAllCells() {
        return adjacencyEdgeMap.keySet();
    }

    /**
     * Получает тип ребра между двумя ячейками.
     *
     * @param from Начальная ячейка.
     * @param to   Конечная ячейка.
     * @return Тип ребра, или null если ребро отсутствует.
     */
    public EdgeType getEdgeType(Cell from, Cell to) {
        List<Edge> edges = adjacencyEdgeMap.get(from);
        if (edges != null) {
            for (Edge edge : edges) {
                if (edge.to().equals(to)) {
                    return edge.type();
                }
            }
        }
        return null;
    }

    /**
     * Обновляет тип ребра между двумя ячейками.
     *
     * @param from    Начальная ячейка.
     * @param to      Конечная ячейка.
     * @param newType Новый тип ребра.
     */
    public void updateEdgeType(Cell from, Cell to, EdgeType newType) {
        List<Edge> edges = adjacencyEdgeMap.get(from);
        if (edges != null) {
            for (Edge edge : edges) {
                if (edge.to().equals(to)) {
                    edge.type(newType);
                }
            }
        }

        // Обновляем обратное ребро
        edges = adjacencyEdgeMap.get(to);
        if (edges != null) {
            for (Edge edge : edges) {
                if (edge.to().equals(from)) {
                    edge.type(newType);
                }
            }
        }
    }

    /**
     * Возвращает список рёбер, исходящих из заданной ячейки.
     *
     * @param cell Ячейка, для которой необходимо получить рёбра.
     * @return Список рёбер, исходящих из ячейки.
     */
    public List<Edge> getEdges(Cell cell) {
        return adjacencyEdgeMap.getOrDefault(cell, new ArrayList<>());
    }
}
