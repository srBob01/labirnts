package backend.academy.solver.priority;

import backend.academy.solver.AbstractSolverTest;
import backend.academy.utils.ReconstructorPath;

public class DijkstraSolverTest extends AbstractSolverTest {

    public DijkstraSolverTest() {
        super(new DijkstraSolver(new ReconstructorPath()), true);
    }
}
