package backend.academy.render;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.cell.CellType;
import backend.academy.entity.cell.Coordinate;
import backend.academy.entity.edge.EdgeType;
import backend.academy.entity.maze.Maze;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс SimpleRender отвечает за текстовое отображение лабиринта с возможностью
 * отображения пути через весь лабиринт. Путь отмечается символом '*', включая проходы через стены.
 */
public class SimpleRender implements Render {
    public static final char SPACE = ' ';
    public static final String H_WALL = "---";
    public static final String SPACES = "   ";
    public static final String GOOD_SURFACE = " G ";
    public static final String BAD_SURFACE = " B ";
    public static final char V_GOOD_TRANSITION = '=';
    public static final char V_BAD_TRANSITION = '~';
    public static final String H_GOOD_TRANSITION = " = ";
    public static final String H_BAD_TRANSITION = " ~ ";
    public static final String H_STAR = " * ";
    public static final char V_STAR = '*';
    public static final char V_WALL = '|';
    private static final char CROSS_HAIR = '+';
    private static final char LINE_BREAK = '\n';

    @Override
    public String render(Maze maze) {
        return renderMazeWithOptionalPath(maze, null);
    }

    @Override
    public String render(Maze maze, List<Coordinate> path) {
        return renderMazeWithOptionalPath(maze, path);
    }

    /**
     * Основной метод для рендеринга лабиринта.
     * Может отображать лабиринт как с путём, так и без него.
     *
     * @param maze Лабиринт для отображения.
     * @param path Путь, который нужно отобразить (или null, если путь не нужен).
     * @return Строковое представление лабиринта.
     */
    private String renderMazeWithOptionalPath(Maze maze, List<Coordinate> path) {
        StringBuilder sb = new StringBuilder();

        Set<Cell> cells = maze.getAllCells();
        if (cells.isEmpty()) {
            return "";
        }

        // Вычисляем границы лабиринта
        MazeBoundaries boundaries = calculateBoundaries(cells);
        int minRow = boundaries.minRow();
        int maxRow = boundaries.maxRow();
        int minCol = boundaries.minCol();
        int maxCol = boundaries.maxCol();

        int height = maxRow - minRow + 1;
        int width = maxCol - minCol + 1;

        Cell[][] grid = createGrid(cells, minRow, minCol, height, width);

        Set<Coordinate> pathSet = (path != null) ? new HashSet<>(path) : null;

        for (int row = 0; row < height; row++) {
            // Верхняя граница ячеек
            sb.append(buildTopLine(grid, maze, row, width, pathSet));

            // Содержимое ячеек и вертикальные стены
            sb.append(buildMiddleLine(grid, maze, row, width, pathSet));
        }

        // Нижняя граница лабиринта
        sb.append(buildBottomLine(width));

        return sb.toString();
    }

    /**
     * Вычисляет границы лабиринта (минимальный и максимальный ряды и столбцы).
     *
     * @param cells Набор всех ячеек в лабиринте.
     * @return Объект MazeBoundaries с минимальными и максимальными значениями.
     */
    private MazeBoundaries calculateBoundaries(Set<Cell> cells) {
        int minRow = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        int minCol = Integer.MAX_VALUE;
        int maxCol = Integer.MIN_VALUE;

        for (Cell cell : cells) {
            int row = cell.coordinate().row();
            int col = cell.coordinate().col();

            if (row < minRow) {
                minRow = row;
            }
            if (row > maxRow) {
                maxRow = row;
            }
            if (col < minCol) {
                minCol = col;
            }
            if (col > maxCol) {
                maxCol = col;
            }
        }

        return new MazeBoundaries(minRow, maxRow, minCol, maxCol);
    }

    /**
     * Создаёт сетку на основе набора ячеек лабиринта.
     *
     * @param cells  Набор всех ячеек.
     * @param minRow Минимальный ряд.
     * @param minCol Минимальный столбец.
     * @param height Высота сетки.
     * @param width  Ширина сетки.
     * @return Двумерный массив с ячейками лабиринта.
     */
    private Cell[][] createGrid(Set<Cell> cells, int minRow, int minCol, int height, int width) {
        Cell[][] grid = new Cell[height][width];

        for (Cell cell : cells) {
            int row = cell.coordinate().row() - minRow;
            int col = cell.coordinate().col() - minCol;
            grid[row][col] = cell;
        }

        return grid;
    }

    /**
     * Построение верхней границы строки.
     *
     * @param grid    Сетка ячеек.
     * @param maze    Лабиринт.
     * @param row     Текущий ряд.
     * @param width   Ширина сетки.
     * @param pathSet Набор координат пути.
     * @return Строка с верхней границей для текущего ряда.
     */
    private String buildTopLine(Cell[][] grid, Maze maze, int row, int width, Set<Coordinate> pathSet) {
        StringBuilder sb = new StringBuilder();
        for (int col = 0; col < width; col++) {
            sb.append(CROSS_HAIR);
            Cell currentCell = grid[row][col];
            if (row == 0 || currentCell == null) {
                sb.append(H_WALL);
            } else {
                Cell aboveCell = grid[row - 1][col];
                EdgeType edgeType = maze.getEdgeType(currentCell, aboveCell);
                sb.append(getHorizontalEdgeSymbol(edgeType, currentCell.coordinate(), aboveCell.coordinate(), pathSet));
            }
        }
        sb.append(CROSS_HAIR).append(LINE_BREAK);
        return sb.toString();
    }

    /**
     * Построение середины строки (с ячейками и вертикальными границами).
     *
     * @param grid    Сетка ячеек.
     * @param maze    Лабиринт.
     * @param row     Текущий ряд.
     * @param width   Ширина сетки.
     * @param pathSet Множество координат пути.
     * @return Строка с содержимым ячеек и вертикальными границами для текущего ряда.
     */
    private String buildMiddleLine(Cell[][] grid, Maze maze, int row, int width, Set<Coordinate> pathSet) {
        StringBuilder sb = new StringBuilder();
        for (int col = 0; col < width; col++) {
            Cell currentCell = grid[row][col];
            if (col == 0 || currentCell == null) {
                sb.append(V_WALL);
            } else {
                Cell leftCell = grid[row][col - 1];
                EdgeType edgeType = maze.getEdgeType(currentCell, leftCell);
                sb.append(getVerticalEdgeSymbol(edgeType, currentCell.coordinate(), leftCell.coordinate(), pathSet));
            }

            if (currentCell == null) {
                sb.append(SPACES);
            } else {
                Coordinate coord = currentCell.coordinate();
                if (pathSet != null && pathSet.contains(coord)) {
                    sb.append(H_STAR);
                } else {
                    sb.append(getSymbolForCell(currentCell.cellType()));
                }
            }
        }
        sb.append(V_WALL).append(LINE_BREAK);
        return sb.toString();
    }

    /**
     * Построение нижней границы.
     *
     * @param width Ширина сетки.
     * @return Строка с нижней границей лабиринта.
     */
    private String buildBottomLine(int width) {
        return (CROSS_HAIR + H_WALL).repeat(width) + CROSS_HAIR + LINE_BREAK;
    }

    /**
     * Возвращает символ, соответствующий типу ячейки.
     *
     * @param cellType Тип ячейки.
     * @return Символ, соответствующий типу ячейки.
     */
    private String getSymbolForCell(CellType cellType) {
        return switch (cellType) {
            case PASSAGE -> SPACES;
            case GOOD_SURFACE -> GOOD_SURFACE;
            case BAD_SURFACE -> BAD_SURFACE;
        };
    }

    /**
     * Возвращает символ для горизонтального ребра между ячейками.
     *
     * @param edgeType Тип ребра.
     * @param coord1   Координаты первой ячейки.
     * @param coord2   Координаты второй ячейки.
     * @param pathSet  Набор координат пути.
     * @return Символ, соответствующий горизонтальному ребру.
     */
    private String getHorizontalEdgeSymbol(
        EdgeType edgeType,
        Coordinate coord1,
        Coordinate coord2,
        Set<Coordinate> pathSet
    ) {
        if (isPathBetweenCells(edgeType, coord1, coord2, pathSet)) {
            return H_STAR;  // Проходимая стена на пути
        }
        return getHorizontalEdgeBasedOnType(edgeType);
    }

    /**
     * Возвращает символ для вертикального ребра между ячейками.
     *
     * @param edgeType Тип ребра.
     * @param coord1   Координаты первой ячейки.
     * @param coord2   Координаты второй ячейки.
     * @param pathSet  Набор координат пути.
     * @return Символ, соответствующий вертикальному ребру.
     */
    private char getVerticalEdgeSymbol(
        EdgeType edgeType,
        Coordinate coord1,
        Coordinate coord2,
        Set<Coordinate> pathSet
    ) {
        if (isPathBetweenCells(edgeType, coord1, coord2, pathSet)) {
            return V_STAR;  // Проходимая стена на пути
        }
        return getVerticalEdgeBasedOnType(edgeType);
    }

    /**
     * Проверяет, проходит ли путь между двумя ячейками через проходимое ребро.
     *
     * @param edgeType Тип ребра.
     * @param coord1   Координаты первой ячейки.
     * @param coord2   Координаты второй ячейки.
     * @param pathSet  Набор координат пути.
     * @return true, если путь проходит через проходимую стену, иначе false.
     */
    private boolean isPathBetweenCells(
        EdgeType edgeType,
        Coordinate coord1,
        Coordinate coord2,
        Set<Coordinate> pathSet
    ) {
        return edgeType != null && edgeType.isPassable() && pathSet != null
               && pathSet.contains(coord1) && pathSet.contains(coord2);
    }

    /**
     * Возвращает символ для горизонтального ребра на основе типа ребра.
     *
     * @param edgeType Тип ребра.
     * @return Символ, соответствующий типу ребра.
     */
    private String getHorizontalEdgeBasedOnType(EdgeType edgeType) {
        if (edgeType == null || !edgeType.isPassable()) {
            return H_WALL; // Непроходимая стена
        }
        return switch (edgeType) {
            case GOOD_TRANSITION -> H_GOOD_TRANSITION;
            case BAD_TRANSITION -> H_BAD_TRANSITION;
            default -> SPACES;
        };
    }

    /**
     * Возвращает символ для вертикального ребра на основе типа ребра.
     *
     * @param edgeType Тип ребра.
     * @return Символ, соответствующий типу ребра.
     */
    private char getVerticalEdgeBasedOnType(EdgeType edgeType) {
        if (edgeType == null || !edgeType.isPassable()) {
            return V_WALL; // Непроходимая стена
        }
        return switch (edgeType) {
            case GOOD_TRANSITION -> V_GOOD_TRANSITION;
            case BAD_TRANSITION -> V_BAD_TRANSITION;
            default -> SPACE;
        };
    }

    /**
     * Внутренний record для хранения границ лабиринта.
     */
    private record MazeBoundaries(int minRow, int maxRow, int minCol, int maxCol) {
    }
}
