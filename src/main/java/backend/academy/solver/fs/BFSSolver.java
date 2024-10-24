package backend.academy.solver.fs;

import backend.academy.entity.cell.Cell;
import backend.academy.utils.ReconstructorPath;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Реализация BFS с использованием очереди.
 */
public class BFSSolver extends AbstractSolver {
    private final Queue<Cell> cells = new LinkedList<>();

    public BFSSolver(ReconstructorPath reconstructorPath) {
        super(reconstructorPath);
    }

    @Override
    protected void addToStructure(Cell cell) {
        cells.add(cell);
    }

    @Override
    protected Cell retrieveFromStructure() {
        return cells.poll();
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
