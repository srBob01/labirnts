package backend.academy.solver;

import backend.academy.entity.cell.Coordinate;
import backend.academy.entity.maze.Maze;
import backend.academy.entity.path.Path;
import backend.academy.mazetype.MazeTypeProvider;

/**
 * Интерфейс для алгоритмов решения лабиринта.
 */
public interface Solver {
    /**
     * Находит путь в заданном лабиринте от начальной до конечной координаты.
     *
     * @param maze             Лабиринт для решения.
     * @param start            Начальная координата.
     * @param end              Конечная координата.
     * @param mazeTypeProvider Провайдер типов лабиринта.
     * @return Объект Path, содержащий координаты пути и общую стоимость.
     */
    Path solve(Maze maze, Coordinate start, Coordinate end, MazeTypeProvider mazeTypeProvider);
}
