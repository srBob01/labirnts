package backend.academy.solver.fs;

import backend.academy.entity.cell.Cell;
import backend.academy.utils.ReconstructorPath;
import java.util.Stack;

/**
 * Реализация DFS с использованием стека.
 */
public class DFSSolver extends AbstractSolver {
    private final Stack<Cell> stack = new Stack<>();

    public DFSSolver(ReconstructorPath reconstructorPath) {
        super(reconstructorPath);
    }

    @Override
    protected void addToStructure(Cell cell) {
        stack.push(cell);
    }

    @Override
    protected Cell retrieveFromStructure() {
        return stack.pop();
    }

    @Override
    protected boolean isStructureEmpty() {
        return stack.isEmpty();
    }

    @Override
    protected void clearStructure() {
        stack.clear();
    }
}
