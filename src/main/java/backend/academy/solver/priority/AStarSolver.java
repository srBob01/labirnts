package backend.academy.solver.priority;

import backend.academy.entity.cell.Cell;
import backend.academy.utils.ReconstructorPath;

/**
 * Реализация алгоритма A*, где приоритет основывается на стоимости пути (g-счёт) и эвристике (h-счёт).
 */
public class AStarSolver extends AbstractPrioritySolver {

    public AStarSolver(ReconstructorPath reconstructorPath) {
        super(reconstructorPath);
    }

    @Override
    protected int calculatePriority(Cell cell, Cell endCell, int gScore) {
        // Для A* приоритетом является сумма g-счёта и эвристики (манхэттенское расстояние)
        int hScore = heuristic(cell, endCell);
        return gScore + hScore;
    }

    /**
     * Эвристика: манхэттенское расстояние между двумя ячейками.
     *
     * @param a Первая ячейка.
     * @param b Вторая ячейка.
     * @return Манхэттенское расстояние.
     */
    private int heuristic(Cell a, Cell b) {
        int dx = Math.abs(a.coordinate().row() - b.coordinate().row());
        int dy = Math.abs(a.coordinate().col() - b.coordinate().col());
        return dx + dy;
    }
}
