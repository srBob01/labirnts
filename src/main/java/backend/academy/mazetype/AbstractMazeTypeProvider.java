package backend.academy.mazetype;

import backend.academy.entity.cell.CellType;
import backend.academy.entity.edge.EdgeType;
import backend.academy.random.RandomGenerator;
import java.util.Arrays;

public class AbstractMazeTypeProvider implements MazeTypeProvider {
    private final CellType[] types;
    private final EdgeType[] passableTypes;
    private final EdgeType[] wallTypes;

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
        this.types = types;
        this.passableTypes = passableTypes;
        this.wallTypes = wallTypes;
        this.randomGenerator = randomGenerator;
    }

    @Override
    public CellType getCellType() {
        if (types.length == 1) {
            return types[0];
        }
        return types[randomGenerator.nextInt(types.length)];
    }

    @Override
    public EdgeType getPassableEdgeType() {
        if (passableTypes.length == 1) {
            return passableTypes[0];
        }
        return passableTypes[randomGenerator.nextInt(passableTypes.length)];
    }

    @Override
    public EdgeType getUnPassableEdgeType() {
        if (wallTypes.length == 1) {
            return wallTypes[0];
        }
        return wallTypes[randomGenerator.nextInt(wallTypes.length)];
    }

    @Override
    public boolean isWall(EdgeType type) {
        return Arrays.stream(wallTypes).anyMatch(wall -> wall == type);
    }

    @Override
    public boolean isPassage(EdgeType type) {
        return Arrays.stream(passableTypes).anyMatch(passable -> passable == type);
    }
}
