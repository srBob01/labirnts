package backend.academy.mazetype;

import backend.academy.entity.cell.CellType;
import backend.academy.entity.edge.EdgeType;
import backend.academy.random.RandomGenerator;

/**
 * Расширенная реализация {@link MazeTypeProvider}, позволяющая использовать различные типы ячеек и рёбер.
 * Использует генератор случайных чисел для выбора типов ячеек и рёбер.
 */
public final class AdvancedMazeTypeProvider extends AbstractMazeTypeProvider {

    public AdvancedMazeTypeProvider(RandomGenerator randomGenerator) {
        super(CellType.values(),
            new EdgeType[] {EdgeType.TRANSITION, EdgeType.GOOD_TRANSITION, EdgeType.BAD_TRANSITION},
            new EdgeType[] {EdgeType.WALL}, randomGenerator);
    }
}
