package backend.academy;

import backend.academy.cycleadder.CycleAdder;
import backend.academy.game.GameFlowManager;
import backend.academy.game.GameIORender;
import backend.academy.game.GameLogic;
import backend.academy.generator.MazeGeneratorFactory;
import backend.academy.input.ConsoleInputReader;
import backend.academy.mazetype.MazeTypeProviderFactory;
import backend.academy.output.ConsoleOutputWriter;
import backend.academy.random.RandomGenerator;
import backend.academy.random.SimpleRandomGenerator;
import backend.academy.render.SimpleRender;
import backend.academy.solver.SolverFactory;
import backend.academy.utils.MazeBoundarySelector;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        RandomGenerator rng = new SimpleRandomGenerator();
        final var gameFlowManager = new GameFlowManager();
        final var inputInterface = new ConsoleInputReader();
        final var outputInterface = new ConsoleOutputWriter();
        final var gameIORender = new GameIORender(inputInterface, outputInterface, rng);
        final var gameLogic = getGameLogic(rng, gameFlowManager, gameIORender);
        gameLogic.startGame();

    }

    private static GameLogic getGameLogic(
        RandomGenerator rng,
        GameFlowManager gameFlowManager,
        GameIORender gameIORender
    ) {
        final var cycleAdder = new CycleAdder(rng);
        final var render = new SimpleRender();
        final var boundarySelector = new MazeBoundarySelector();
        final var mazeGeneratorFactory = new MazeGeneratorFactory(rng);
        final var mazeTypeProviderFactory = new MazeTypeProviderFactory(rng);
        final var solverFactory = new SolverFactory();
        return new GameLogic(gameFlowManager, gameIORender, cycleAdder, render,
            boundarySelector, mazeGeneratorFactory, mazeTypeProviderFactory, solverFactory);
    }
}
