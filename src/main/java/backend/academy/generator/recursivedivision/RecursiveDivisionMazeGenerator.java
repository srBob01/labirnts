package backend.academy.generator.recursivedivision;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.maze.Maze;
import backend.academy.generator.Generator;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.random.RandomGenerator;
import backend.academy.utils.MazeUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecursiveDivisionMazeGenerator implements Generator {
    private final RandomGenerator randomGenerator;
    private final MazeUtils mazeUtils;

    /**
     * Основной метод генерации лабиринта. Инициализирует сетку ячеек и начинает процесс рекурсивного деления.
     *
     * @param height       высота лабиринта
     * @param width        ширина лабиринта
     * @param typeProvider провайдер типов ячеек и рёбер для создания лабиринта
     * @return объект Maze, представляющий сгенерированный лабиринт
     */
    @Override
    public Maze generate(int height, int width, MazeTypeProvider typeProvider) {
        if (height < 1 || width < 1) {
            throw new IllegalArgumentException("Размеры лабиринта должны быть положительными.");
        }

        Maze maze = new Maze();
        Cell[][] grid = new Cell[height][width];

        // Инициализация сетки с проходами вместо стен
        initializeGridWithoutWalls(height, width, grid, maze, typeProvider);

        // Запуск рекурсивного деления для создания стен
        divide(maze, typeProvider, grid, 0, 0, width, height);

        return maze;
    }

    /**
     * Инициализация сетки лабиринта. Устанавливает все рёбра между соседними ячейками как проходные.
     *
     * @param height       высота лабиринта
     * @param width        ширина лабиринта
     * @param grid         двумерный массив ячеек лабиринта
     * @param maze         объект Maze для хранения структуры лабиринта
     * @param typeProvider провайдер типов ячеек и рёбер
     */
    private void initializeGridWithoutWalls(
        int height,
        int width,
        Cell[][] grid,
        Maze maze,
        MazeTypeProvider typeProvider
    ) {
        mazeUtils.initializeGrid(height, width, grid, maze, typeProvider);

        // Устанавливаем все рёбра как проходные, чтобы изначально не было стен
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = grid[row][col];
                if (row > 0) {
                    Cell above = grid[row - 1][col];
                    maze.addEdge(cell, above, typeProvider.getPassableEdgeType());
                }
                if (col > 0) {
                    Cell left = grid[row][col - 1];
                    maze.addEdge(cell, left, typeProvider.getPassableEdgeType());
                }
            }
        }
    }

    /**
     * Рекурсивно делит пространство лабиринта, создавая стены и оставляя случайные проходы.
     *
     * @param maze         объект Maze для хранения структуры лабиринта
     * @param typeProvider провайдер типов ячеек и рёбер
     * @param grid         двумерный массив ячеек лабиринта
     * @param x            координата X начала области деления
     * @param y            координата Y начала области деления
     * @param width        ширина области деления
     * @param height       высота области деления
     */
    private void divide(Maze maze, MazeTypeProvider typeProvider, Cell[][] grid, int x, int y, int width, int height) {
        // Базовый случай: если область слишком мала, прекращаем деление
        if (width <= 2 || height <= 2) {
            return;
        }

        // Выбираем, будет ли стена горизонтальной или вертикальной
        boolean horizontal = width < height;

        int wx;
        int wy;

        // Определяем координаты начала стены
        if (horizontal) {
            wx = x;
            wy = y + 1 + randomGenerator.nextInt(height - 2);
        } else {
            wx = x + 1 + randomGenerator.nextInt(width - 2);
            wy = y;
        }

        // Определяем случайный проход в стене
        int px;
        int py;
        if (horizontal) {
            px = wx + randomGenerator.nextInt(width);
            py = wy;
        } else {
            px = wx;
            py = wy + randomGenerator.nextInt(height);
        }

        int dx = horizontal ? 1 : 0;
        int dy = horizontal ? 0 : 1;

        int length = horizontal ? width : height;
        int wallLength = length - 1;

        // Строим стену с одним случайным проходом
        for (int i = 0; i < wallLength; i++) {
            int nx = wx + i * dx;
            int ny = wy + i * dy;
            if (nx != px || ny != py) {
                Cell cell1 = grid[ny][nx];
                Cell cell2 = grid[ny + dy][nx + dx];

                mazeUtils.setUnpassableEdgeAndReverse(maze, cell1, cell2, typeProvider);
            }
        }

        // Рекурсивное деление на две части
        if (horizontal) {
            divide(maze, typeProvider, grid, x, y, width, wy - y);
            divide(maze, typeProvider, grid, x, wy + 1, width, y + height - wy - 1);
        } else {
            divide(maze, typeProvider, grid, x, y, wx - x, height);
            divide(maze, typeProvider, grid, wx + 1, y, x + width - wx - 1, height);
        }
    }
}
