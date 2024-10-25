package backend.academy.solver.priority;

import backend.academy.solver.AbstractSolverTest;
import backend.academy.utils.ReconstructorPath;

public class AStarSolverTest extends AbstractSolverTest {

    public AStarSolverTest() {
        super(new AStarSolver(new ReconstructorPath()), true);
    }
}
