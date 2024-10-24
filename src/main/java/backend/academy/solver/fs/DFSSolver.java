package backend.academy.solver.fs;

import backend.academy.entity.cell.Cell;
import backend.academy.utils.ReconstructorPath;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Реализация DFS с использованием Deque.
 */
public class DFSSolver extends AbstractSolver {
    private final Deque<Cell> cells = new ArrayDeque<>();

    public DFSSolver(ReconstructorPath reconstructorPath) {
        super(reconstructorPath);
    }

    @Override
    protected void addToStructure(Cell cell) {
        cells.push(cell);
    }

    @Override
    protected Cell retrieveFromStructure() {
        return cells.pop();
    }

    @Override
    protected boolean isStructureEmpty() {
        return cells.isEmpty();
    }

    @Override
    protected void clearStructure() {
        cells.clear();
    }
}
