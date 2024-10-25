package backend.academy.generator.recursivedivision;

import backend.academy.generator.AbstractMazeGeneratorTest;
import backend.academy.generator.Generator;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.mazetype.SimpleMazeTypeProvider;
import backend.academy.random.RandomGenerator;
import backend.academy.random.SimpleRandomGenerator;
import backend.academy.utils.MazeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class RecursiveDivisionMazeGeneratorTest extends AbstractMazeGeneratorTest {

    private RecursiveDivisionMazeGenerator generator;
    private MazeTypeProvider mazeTypeProvider;

    public RecursiveDivisionMazeGeneratorTest() {
        super(true);  // Recursive Division Maze должен быть цикличным
    }

    @BeforeEach
    public void setup() {
        RandomGenerator randomGenerator = new SimpleRandomGenerator();
        MazeUtils mazeUtils = new MazeUtils();
        mazeTypeProvider = new SimpleMazeTypeProvider();
        generator = new RecursiveDivisionMazeGenerator(randomGenerator, mazeUtils);
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
