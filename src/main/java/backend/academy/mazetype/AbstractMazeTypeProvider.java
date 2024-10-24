package backend.academy.mazetype;

import backend.academy.entity.cell.CellType;
import backend.academy.entity.edge.EdgeType;
import backend.academy.random.RandomGenerator;
import java.util.Arrays;

public class AbstractMazeTypeProvider implements MazeTypeProvider {
    private final CellType[] TYPES;
    private final EdgeType[] PASSABLE_TYPES;
    private final EdgeType[] WALL_TYPES;

    private final RandomGenerator randomGenerator;

    /**
     * Конструктор для {@link AbstractMazeTypeProvider}.
     *
     * @param types           Массив типов ячеек.
     * @param passableTypes   Массив проходимых типов рёбер.
     * @param wallTypes       Массив непроходимых типов рёбер.
     * @param randomGenerator Экземпляр {@link RandomGenerator} для генерации случайных типов.
     */
    protected AbstractMazeTypeProvider(
        CellType[] types, EdgeType[] passableTypes, EdgeType[] wallTypes,
        RandomGenerator randomGenerator
    ) {
        TYPES = types;
        PASSABLE_TYPES = passableTypes;
        WALL_TYPES = wallTypes;
        this.randomGenerator = randomGenerator;
    }

    @Override
    public CellType getCellType() {
        if (TYPES.length == 1) {
            return TYPES[0];
        }
        return TYPES[randomGenerator.nextInt(TYPES.length)];
    }

    @Override
    public EdgeType getPassableEdgeType() {
        if (PASSABLE_TYPES.length == 1) {
            return PASSABLE_TYPES[0];
        }
        return PASSABLE_TYPES[randomGenerator.nextInt(PASSABLE_TYPES.length)];
    }

    @Override
    public EdgeType getUnPassableEdgeType() {
        if (WALL_TYPES.length == 1) {
            return WALL_TYPES[0];
        }
        return WALL_TYPES[randomGenerator.nextInt(WALL_TYPES.length)];
    }

    @Override
    public boolean isWall(EdgeType type) {
        return Arrays.stream(WALL_TYPES).anyMatch(wall -> wall == type);
    }

    @Override
    public boolean isPassage(EdgeType type) {
        return Arrays.stream(PASSABLE_TYPES).anyMatch(passable -> passable == type);
    }
}
