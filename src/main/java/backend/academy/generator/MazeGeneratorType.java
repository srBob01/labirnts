package backend.academy.generator;

import lombok.RequiredArgsConstructor;

/**
 * Enum для выбора алгоритма генерации лабиринта.
 */
@RequiredArgsConstructor
public enum MazeGeneratorType {
    GROWING_TREE("Growing Tree Algorithm"),
    HUNT_AND_KILL("Hunt and Kill Algorithm"),
    KRUSKAL("Kruskal's Algorithm"),
    PRIM("Prim's Algorithm"),
    RECURSIVE_DIVISION("Recursive Division Algorithm");

    private final String description;

    @Override
    public String toString() {
        return description;
    }
}
