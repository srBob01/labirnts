package backend.academy.generator.kruskal;

import backend.academy.generator.AbstractMazeGeneratorTest;
import backend.academy.generator.Generator;
import backend.academy.mazetype.MazeTypeProvider;
import backend.academy.mazetype.SimpleMazeTypeProvider;
import backend.academy.utils.MazeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class KruskalMazeGeneratorTest extends AbstractMazeGeneratorTest {

    private KruskalMazeGenerator generator;
    private MazeTypeProvider mazeTypeProvider;

    public KruskalMazeGeneratorTest() {
        super(false);  // Kruskal Maze должен быть ацикличным
    }

    @BeforeEach
    public void setup() {
        MazeUtils mazeUtils = new MazeUtils();
        mazeTypeProvider = new SimpleMazeTypeProvider();
        generator = new KruskalMazeGenerator(mazeUtils);
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
