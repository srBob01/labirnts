package backend.academy.generator;

import backend.academy.generator.growingtree.GrowingTreeMazeGenerator;
import backend.academy.generator.growingtree.SelectionStrategyGrowingTree;
import backend.academy.generator.huntandkill.HuntAndKillMazeGenerator;
import backend.academy.generator.kruskal.KruskalMazeGenerator;
import backend.academy.generator.prime.PrimMazeGenerator;
import backend.academy.generator.recursivedivision.RecursiveDivisionMazeGenerator;
import backend.academy.random.RandomGenerator;
import backend.academy.utils.MazeUtils;

public class MazeGeneratorFactory {

    private final Generator growingTreeGenerator;
    private final Generator huntAndKillGenerator;
    private final Generator kruskalGenerator;
    private final Generator primGenerator;
    private final Generator recursiveDivisionGenerator;

    public MazeGeneratorFactory(RandomGenerator randomGenerator) {
        MazeUtils mazeUtils = new MazeUtils();
        this.growingTreeGenerator =
            new GrowingTreeMazeGenerator(randomGenerator, mazeUtils, SelectionStrategyGrowingTree.RANDOM);
        this.huntAndKillGenerator = new HuntAndKillMazeGenerator(randomGenerator, mazeUtils);
        this.kruskalGenerator = new KruskalMazeGenerator(randomGenerator, mazeUtils);
        this.primGenerator = new PrimMazeGenerator(randomGenerator, mazeUtils);
        this.recursiveDivisionGenerator = new RecursiveDivisionMazeGenerator(randomGenerator, mazeUtils);
    }

    /**
     * Возвращает реализацию генератора лабиринта на основе переданного типа MazeGeneratorType.
     *
     * @param generatorType Тип генератора лабиринта.
     * @return Объект, реализующий интерфейс Generator.
     */
    public Generator getGenerator(MazeGeneratorType generatorType) {
        return switch (generatorType) {
            case GROWING_TREE -> growingTreeGenerator;
            case HUNT_AND_KILL -> huntAndKillGenerator;
            case KRUSKAL -> kruskalGenerator;
            case PRIM -> primGenerator;
            case RECURSIVE_DIVISION -> recursiveDivisionGenerator;
        };
    }
}
