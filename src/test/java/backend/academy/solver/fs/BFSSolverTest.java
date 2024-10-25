package backend.academy.solver.fs;

import backend.academy.solver.AbstractSolverTest;
import backend.academy.utils.ReconstructorPath;

public class BFSSolverTest extends AbstractSolverTest {

    public BFSSolverTest() {
        super(new BFSSolver(new ReconstructorPath()), false);
    }
}
