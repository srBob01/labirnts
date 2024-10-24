package backend.academy.solver;

import backend.academy.solver.bidirection.BiDirectionalSolver;
import backend.academy.solver.fs.BFSSolver;
import backend.academy.solver.fs.DFSSolver;
import backend.academy.solver.priority.AStarSolver;
import backend.academy.solver.priority.DijkstraSolver;
import backend.academy.utils.ReconstructorPath;

public class SolverFactory {

    private final Solver bfsSolver;
    private final Solver dfsSolver;
    private final Solver dijkstraSolver;
    private final Solver aStarSolver;
    private final Solver biDirectionalSolver;

    public SolverFactory() {
        ReconstructorPath reconstructorPath = new ReconstructorPath();
        this.bfsSolver = new BFSSolver(reconstructorPath);
        this.dfsSolver = new DFSSolver(reconstructorPath);
        this.dijkstraSolver = new DijkstraSolver(reconstructorPath);
        this.aStarSolver = new AStarSolver(reconstructorPath);
        this.biDirectionalSolver = new BiDirectionalSolver();
    }

    /**
     * Возвращает реализацию алгоритма решения лабиринта в зависимости от переданного типа SolverType.
     *
     * @param solverType Тип алгоритма.
     * @return Объект, реализующий интерфейс Solver.
     */
    public Solver getSolver(SolverType solverType) {
        return switch (solverType) {
            case BFS -> bfsSolver;
            case DFS -> dfsSolver;
            case DIJKSTRA -> dijkstraSolver;
            case A_STAR -> aStarSolver;
            case BIDIRECTIONAL -> biDirectionalSolver;
            case ALL -> null;
        };
    }
}
