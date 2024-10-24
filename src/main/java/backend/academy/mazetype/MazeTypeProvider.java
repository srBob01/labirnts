package backend.academy.mazetype;

import backend.academy.entity.cell.CellType;
import backend.academy.entity.edge.EdgeType;

/**
 * Интерфейс предоставляет методы для определения типов ячеек и рёбер в лабиринте.
 * Также включает методы для проверки, является ли определённый тип рёбер стеной или проходимым переходом.
 */
public interface MazeTypeProvider {

    /**
     * Получает тип ячейки.
     *
     * @return {@link CellType} представляющий тип ячейки.
     */
    CellType getCellType();

    /**
     * Получает тип проходимого рёбра.
     *
     * @return {@link EdgeType} представляющий проходимый тип рёбра.
     */
    EdgeType getPassableEdgeType();

    /**
     * Получает тип непроходимого рёбра.
     *
     * @return {@link EdgeType} представляющий непроходимый тип рёбра.
     */
    EdgeType getUnPassableEdgeType();

    /**
     * Проверяет, является ли заданный тип рёбра стеной.
     *
     * @param type {@link EdgeType} тип рёбра для проверки.
     * @return {@code true}, если тип рёбра является стеной, иначе {@code false}.
     */
    boolean isWall(EdgeType type);

    /**
     * Проверяет, является ли заданный тип рёбра проходимым переходом.
     *
     * @param type {@link EdgeType} тип рёбра для проверки.
     * @return {@code true}, если тип рёбра является проходимым переходом, иначе {@code false}.
     */
    boolean isPassage(EdgeType type);
}
