package backend.academy.solver;

import lombok.RequiredArgsConstructor;


/**
 * Enum для выбора алгоритма решения лабиринта.
 */
@RequiredArgsConstructor
public enum SolverType {
    BFS("Breadth-First Search (BFS)"),
    DFS("Depth-First Search (DFS)"),
    DIJKSTRA("Dijkstra's Algorithm"),
    A_STAR("A* Algorithm"),
    BIDIRECTIONAL("Bidirectional Search"),
    ALL("All Algorithms");

    private final String description;

    @Override
    public String toString() {
        return description;
    }
}
