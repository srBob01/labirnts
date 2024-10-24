package backend.academy.generator.growingtree;

/**
 * Определяет стратегию для выбора активной ячейки в алгоритме Growing Tree.
 */
public enum SelectionStrategyGrowingTree {
    RANDOM,   // Случайный выбор активной ячейки
    LAST,     // Последняя добавленная (поиск в глубину)
    FIRST,    // Первая добавленная (поиск в ширину)
}
