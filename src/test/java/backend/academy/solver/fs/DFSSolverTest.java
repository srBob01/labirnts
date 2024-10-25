package backend.academy.solver.fs;

import backend.academy.solver.AbstractSolverTest;
import backend.academy.utils.ReconstructorPath;

public class DFSSolverTest extends AbstractSolverTest {

    public DFSSolverTest() {
        super(new DFSSolver(new ReconstructorPath()), false);
    }
}
