package backend.academy.solver.fs;

import backend.academy.entity.cell.Cell;
import backend.academy.utils.ReconstructorPath;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Реализация BFS с использованием очереди.
 */
public class BFSSolver extends AbstractSolver {
    private final Queue<Cell> queue = new LinkedList<>();

    public BFSSolver(ReconstructorPath reconstructorPath) {
        super(reconstructorPath);
    }

    @Override
    protected void addToStructure(Cell cell) {
        queue.add(cell);
    }

    @Override
    protected Cell retrieveFromStructure() {
        return queue.poll();
    }

    @Override
    protected boolean isStructureEmpty() {
        return queue.isEmpty();
    }

    @Override
    protected void clearStructure() {
        queue.clear();
    }
}
