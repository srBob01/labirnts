package backend.academy.solver.fs;

import backend.academy.entity.cell.Cell;
import backend.academy.utils.ReconstructorPath;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Реализация DFS с использованием Deque.
 */
public class DFSSolver extends AbstractSolver {
    private final Deque<Cell> deque = new ArrayDeque<>();

    public DFSSolver(ReconstructorPath reconstructorPath) {
        super(reconstructorPath);
    }

    @Override
    protected void addToStructure(Cell cell) {
        deque.push(cell);
    }

    @Override
    protected Cell retrieveFromStructure() {
        return deque.pop();
    }

    @Override
    protected boolean isStructureEmpty() {
        return deque.isEmpty();
    }

    @Override
    protected void clearStructure() {
        deque.clear();
    }
}
