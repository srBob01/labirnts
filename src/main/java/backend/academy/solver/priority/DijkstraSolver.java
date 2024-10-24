package backend.academy.solver.priority;

import backend.academy.entity.cell.Cell;
import backend.academy.utils.ReconstructorPath;

/**
 * Реализация алгоритма Дейкстры, где приоритет основывается только на стоимости пути.
 */
public class DijkstraSolver extends AbstractPrioritySolver {

    public DijkstraSolver(ReconstructorPath reconstructorPath) {
        super(reconstructorPath);
    }

    @Override
    protected int calculatePriority(Cell cell, Cell endCell, int gScore) {
        return gScore;
    }
}
