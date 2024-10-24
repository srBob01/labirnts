package backend.academy.generator;

import backend.academy.entity.maze.Maze;
import backend.academy.mazetype.MazeTypeProvider;

/**
 * Интерфейс для генераторов лабиринтов.
 */
public interface Generator {
    /**
     * Генерирует лабиринт заданного размера с использованием указанного провайдера типов.
     *
     * @param height Высота лабиринта.
     * @param width  Ширина лабиринта.
     * @param type   Провайдер типов заполнения лабиринта.
     * @return Сгенерированный лабиринт.
     */
    Maze generate(int height, int width, MazeTypeProvider type);
}
