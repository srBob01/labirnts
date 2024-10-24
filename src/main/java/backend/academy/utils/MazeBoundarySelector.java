package backend.academy.utils;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.cell.Coordinate;
import backend.academy.entity.maze.Maze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MazeBoundarySelector {

    /**
     * Возвращает координаты случайной вершины из первой строки лабиринта с возможным проходом.
     *
     * @param maze   Лабиринт.
     * @param height Высота лабиринта.
     * @param width  Ширина лабиринта.
     * @return Координаты случайной вершины.
     */
    public Coordinate selectRandomFromFirstRow(Maze maze, int width) {
        return selectRandomBoundaryCell(maze, 0, width);
    }

    /**
     * Возвращает координаты случайной вершины из последней строки лабиринта с возможным проходом.
     *
     * @param maze   Лабиринт.
     * @param height Высота лабиринта.
     * @param width  Ширина лабиринта.
     * @return Координаты случайной вершины.
     */
    public Coordinate selectRandomFromLastRow(Maze maze, int height, int width) {
        return selectRandomBoundaryCell(maze, height - 1, width);
    }

    /**
     * Общий метод для поиска случайной вершины в указанной строке, которая имеет проходы (не только стены вокруг).
     *
     * @param maze  Лабиринт.
     * @param row   Номер строки для поиска.
     * @param width Ширина лабиринта.
     * @return Координаты случайной вершины.
     */
    private Coordinate selectRandomBoundaryCell(Maze maze, int row, int width) {
        List<Cell> boundaryCells = new ArrayList<>();
        for (int col = 0; col < width; col++) {
            Cell cell = maze.getCell(new Coordinate(row, col));
            if (cell != null && hasPassage(maze, cell)) {
                boundaryCells.add(cell);
            }
        }

        Collections.shuffle(boundaryCells);
        return boundaryCells.getFirst().coordinate();
    }

    /**
     * Проверяет, есть ли у ячейки хотя бы один проход (не только стены вокруг).
     *
     * @param maze Лабиринт.
     * @param cell Ячейка для проверки.
     * @return true, если есть хотя бы один проход, иначе false.
     */
    private boolean hasPassage(Maze maze, Cell cell) {
        return !maze.getNeighbors(cell).isEmpty(); // Возвращает true, если есть хотя бы один проходной сосед.
    }
}
