package backend.academy.mazetype;

import backend.academy.entity.cell.CellType;
import backend.academy.entity.edge.EdgeType;
import backend.academy.random.FixedRandomGenerator;

/**
 * Простая реализация {@link MazeTypeProvider}, использующая только {@code PASSAGE} для ячеек
 * и {@code TRANSITION} для рёбер.
 */
public final class SimpleMazeTypeProvider extends AbstractMazeTypeProvider {

    public SimpleMazeTypeProvider() {
        super(new CellType[] {CellType.PASSAGE}, new EdgeType[] {EdgeType.TRANSITION}, new EdgeType[] {EdgeType.WALL},
            new FixedRandomGenerator());
    }
}
