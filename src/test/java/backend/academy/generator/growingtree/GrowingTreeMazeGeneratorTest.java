package backend.academy.generator.growingtree;

import backend.academy.generator.AbstractMazeGeneratorTest;
import backend.academy.generator.Generator;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.mazetype.SimpleMazeTypeProvider;
import backend.academy.random.RandomGenerator;
import backend.academy.random.SimpleRandomGenerator;
import backend.academy.utils.MazeUtils;
import org.junit.jupiter.api.BeforeEach;

public class GrowingTreeMazeGeneratorTest extends AbstractMazeGeneratorTest {

    private GrowingTreeMazeGenerator generator;
    private MazeTypeProvider mazeTypeProvider;

    public GrowingTreeMazeGeneratorTest() {
        super(false);  // Growing Tree Maze должен быть ацикличным
    }

    @BeforeEach
    public void setup() {
        RandomGenerator randomGenerator = new SimpleRandomGenerator();
        MazeUtils mazeUtils = new MazeUtils();
        mazeTypeProvider = new SimpleMazeTypeProvider();
        generator = new GrowingTreeMazeGenerator(randomGenerator, mazeUtils, SelectionStrategyGrowingTree.RANDOM);
    }

    @Override
    protected Generator getGenerator() {
        return generator;
    }

    @Override
    protected MazeTypeProvider getMazeTypeProvider() {
        return mazeTypeProvider;
    }
}
