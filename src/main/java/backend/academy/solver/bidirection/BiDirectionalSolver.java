package backend.academy.solver.bidirection;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.cell.Coordinate;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.maze.Maze;
import backend.academy.entity.path.Path;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.solver.Solver;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BiDirectionalSolver implements Solver {

    @Override
    public Path solve(Maze maze, Coordinate startCoord, Coordinate endCoord, MazeTypeProvider mazeTypeProvider) {
        Cell startCell = maze.getCell(startCoord);
        Cell endCell = maze.getCell(endCoord);

        if (startCell == null || endCell == null) {
            throw new IllegalArgumentException("Начальная или конечная ячейка не найдена в лабиринте.");
        }

        if (startCell.equals(endCell)) {
            return new Path(Collections.singletonList(startCoord), 0);
        }

        // Очереди для прямого и обратного поиска
        Queue<Cell> forwardQueue = new LinkedList<>();
        Queue<Cell> backwardQueue = new LinkedList<>();

        // Посещённые ячейки
        Map<Cell, Integer> forwardVisited = new HashMap<>();
        Map<Cell, Integer> backwardVisited = new HashMap<>();

        // Карты предшественников
        Map<Cell, Cell> forwardPredecessors = new HashMap<>();
        Map<Cell, Cell> backwardPredecessors = new HashMap<>();

        forwardQueue.add(startCell);
        forwardVisited.put(startCell, 0);

        backwardQueue.add(endCell);
        backwardVisited.put(endCell, 0);

        Cell meetingCell = null;

        while (!forwardQueue.isEmpty() && !backwardQueue.isEmpty()) {
            // Прямой поиск
            meetingCell =
                expandFront(maze, mazeTypeProvider, forwardQueue, forwardVisited, forwardPredecessors, backwardVisited);
            if (meetingCell != null) {
                break;
            }

            // Обратный поиск
            meetingCell = expandFront(maze, mazeTypeProvider, backwardQueue, backwardVisited, backwardPredecessors,
                forwardVisited);
            if (meetingCell != null) {
                break;
            }
        }

        if (meetingCell == null) {
            // Путь не найден
            return new Path(Collections.emptyList(), 0);
        }

        List<Coordinate> coordinatesPath = reconstructPath(meetingCell, forwardPredecessors, backwardPredecessors);
        int totalCost = calculateTotalCost(coordinatesPath, maze);

        return new Path(coordinatesPath, totalCost);
    }

    private Cell expandFront(
        Maze maze, MazeTypeProvider mazeTypeProvider,
        Queue<Cell> queue,
        Map<Cell, Integer> visited,
        Map<Cell, Cell> predecessors,
        Map<Cell, Integer> otherVisited
    ) {
        if (queue.isEmpty()) {
            return null;
        }

        Cell current = queue.poll();

        for (Edge edge : maze.getEdges(current)) {
            if (!mazeTypeProvider.isPassage(edge.type())) {
                continue;
            }

            Cell neighbor = edge.to();

            if (!visited.containsKey(neighbor)) {
                visited.put(neighbor, visited.get(current) + edge.type().movementCost());
                predecessors.put(neighbor, current);
                queue.add(neighbor);

                if (otherVisited.containsKey(neighbor)) {
                    // Встреча двух поисков
                    return neighbor;
                }
            }
        }
        return null;
    }

    private List<Coordinate> reconstructPath(
        Cell meetingCell,
        Map<Cell, Cell> forwardPredecessors,
        Map<Cell, Cell> backwardPredecessors
    ) {
        List<Coordinate> path = new LinkedList<>();

        // Восстановление пути от начала до точки встречи
        Cell current = meetingCell;
        while (current != null) {
            path.addFirst(current.coordinate());
            current = forwardPredecessors.get(current);
        }

        // Восстановление пути от точки встречи до конца
        current = backwardPredecessors.get(meetingCell);
        while (current != null) {
            path.add(current.coordinate());
            current = backwardPredecessors.get(current);
        }

        return path;
    }

    private int calculateTotalCost(List<Coordinate> path, Maze maze) {
        int totalCost = 0;
        Cell startCell = maze.getCell(path.getFirst());
        totalCost += startCell.cellType().movementCost();
        for (int i = 0; i < path.size() - 1; i++) {
            Cell from = maze.getCell(path.get(i));
            Cell to = maze.getCell(path.get(i + 1));
            Edge edge = maze.getEdge(from, to);

            if (edge != null) {
                totalCost += edge.type().movementCost() + to.cellType().movementCost();
            }
        }
        return totalCost;
    }
}
