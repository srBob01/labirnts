package backend.academy.game;

import backend.academy.cycleadder.CycleAdder;
import backend.academy.cycleadder.CycleLevelType;
import backend.academy.entity.cell.Coordinate;
import backend.academy.entity.maze.Maze;
import backend.academy.entity.path.Path;
import backend.academy.generator.MazeGeneratorFactory;
import backend.academy.generator.MazeGeneratorType;
import backend.academy.mazetype.MazeTypeProviderFactory;
import backend.academy.mazetype.MazeTypeProviderType;
import backend.academy.render.Render;
import backend.academy.solver.SolverFactory;
import backend.academy.solver.SolverType;
import backend.academy.utils.MazeBoundarySelector;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameLogic {

    private static final Logger LOGGER = Logger.getLogger(GameLogic.class.getName());

    private final GameFlowManager gameFlowManager;
    private final GameIORender gameIORender;
    private final CycleAdder cycleAdder;
    private final Render render;
    private final MazeBoundarySelector boundarySelector;
    private final MazeGeneratorFactory mazeGeneratorFactory;
    private final MazeTypeProviderFactory mazeTypeProviderFactory;
    private final SolverFactory solverFactory;

    private static final int NUMBER_ATTEMPT = 3;
    private static final int MIN_SIDE_MAZE = 3;
    private static final int MAX_SIDE_MAZE = 10000;
    private static final String DELIMITER = " : ";

    /**
     * Главный метод игры, который управляет всем процессом игры.
     */
    public void startGame() {
        LOGGER.info("Starting game...");

        GameStateType currentState = gameFlowManager.startGame();
        MazeTypeProviderType mazeTypeProviderType = MazeTypeProviderType.SIMPLE;
        MazeGeneratorType generatorType = MazeGeneratorType.KRUSKAL;
        CycleLevelType cycleLevelType = CycleLevelType.HIGH;
        SolverType solverType = SolverType.BFS;

        int height = 0;
        int width = 0;
        boolean addCycles = false;

        Maze maze = null;
        Coordinate startPoint = null;
        Coordinate endPoint = null;

        while (!gameFlowManager.isFinished(currentState)) {
            LOGGER.info("Current game state: " + currentState);

            currentState = switch (currentState) {
                case INPUT_PARAMETERS -> {
                    LOGGER.info("Reading maze dimensions...");
                    height =
                        gameIORender.readIntWithRetries(NUMBER_ATTEMPT, MIN_SIDE_MAZE, MAX_SIDE_MAZE, "Enter height");
                    width =
                        gameIORender.readIntWithRetries(NUMBER_ATTEMPT, MIN_SIDE_MAZE, MAX_SIDE_MAZE, "Enter width");

                    mazeTypeProviderType = gameIORender.selectMazeTypeProvider();
                    LOGGER.info("Maze type provider selected: " + mazeTypeProviderType);

                    generatorType = gameIORender.selectMazeGeneratorType();
                    LOGGER.info("Maze generator type selected: " + generatorType);

                    addCycles = generatorType != MazeGeneratorType.RECURSIVE_DIVISION
                                && gameIORender.readBoolean("Should cycles be added? (0 - no, anything else - yes): ");
                    LOGGER.info("Cycles added: " + addCycles);

                    if (addCycles) {
                        cycleLevelType = gameIORender.selectCycleLevelType();
                        LOGGER.info("Cycle level type selected: " + cycleLevelType);
                    }

                    yield gameFlowManager.next(currentState);
                }
                case GENERATE_MAZE -> {
                    LOGGER.info("Generating maze...");
                    var mazeTypeProvider = mazeTypeProviderFactory.getProvider(mazeTypeProviderType);
                    var generator = mazeGeneratorFactory.getGenerator(generatorType);

                    maze = generator.generate(height, width, mazeTypeProvider);
                    LOGGER.info("Maze generated.");

                    if (addCycles) {
                        cycleAdder.addCycles(maze, mazeTypeProvider, cycleLevelType);
                        LOGGER.info("Cycles added to maze.");
                    }

                    gameIORender.print(render.render(maze));

                    yield gameFlowManager.next(currentState);
                }
                case SELECT_POINTS -> {
                    LOGGER.info("Selecting start and end points...");

                    boolean randomSelection =
                        gameIORender.readBoolean("Select a random starting point? (0 - no, anything else - yes): ");
                    if (randomSelection) {
                        startPoint = boundarySelector.selectRandomFromFirstRow(maze, width);
                        endPoint = boundarySelector.selectRandomFromLastRow(maze, height, width);
                        LOGGER.info("Random start point: " + startPoint + " end point: " + endPoint);
                    } else {
                        startPoint = gameIORender.readCoordinates(NUMBER_ATTEMPT, MIN_SIDE_MAZE, width, height);
                        endPoint = gameIORender.readCoordinates(NUMBER_ATTEMPT, MIN_SIDE_MAZE, width, height);
                        LOGGER.info("Manual start point: " + startPoint + ", end point: " + endPoint);
                    }

                    gameIORender.print("Start point: " + startPoint);
                    gameIORender.print("End point: " + endPoint);

                    yield gameFlowManager.next(currentState);
                }
                case SELECT_SOLUTION -> {
                    solverType = gameIORender.selectSolverType();
                    LOGGER.info("Solver type selected: " + solverType);
                    yield gameFlowManager.next(GameStateType.SELECT_SOLUTION);
                }
                case OUTPUT_RESULTS -> {
                    LOGGER.info("Outputting results...");

                    if (solverType == SolverType.ALL) {
                        outputResultsForAllSolutions(maze, startPoint, endPoint, mazeTypeProviderType);
                    } else {
                        outputResults(maze, startPoint, endPoint, solverType, mazeTypeProviderType);
                    }

                    yield gameFlowManager.next(currentState);
                }
                case CHOOSE_NEXT_ACTION -> {
                    LOGGER.info("Choosing next action...");
                    yield gameIORender.selectGameState();
                }
                case FINISH -> {
                    LOGGER.info("Finishing game...");
                    yield gameFlowManager.finishGame();
                }
            };
        }
    }

    private Path solveMaze(
        Maze maze,
        Coordinate startPoint,
        Coordinate endPoint,
        SolverType solverType,
        MazeTypeProviderType mazeTypeProviderType
    ) {
        LOGGER.info("Solving maze with " + solverType);
        var solver = solverFactory.getSolver(solverType);
        return solver.solve(maze, startPoint, endPoint, mazeTypeProviderFactory.getProvider(mazeTypeProviderType));
    }

    private void outputResults(
        Maze maze,
        Coordinate startPoint,
        Coordinate endPoint,
        SolverType solverType,
        MazeTypeProviderType mazeTypeProviderType
    ) {
        Path result = solveMaze(maze, startPoint, endPoint, solverType, mazeTypeProviderType);
        gameIORender.print(render.render(maze, result.coordinates()));
        gameIORender.print(solverType + DELIMITER + result.totalCost());
        LOGGER.info("Result for  " + solverType + ": total cost = " + result.totalCost());
    }

    private void outputResultsForAllSolutions(
        Maze maze,
        Coordinate startPoint,
        Coordinate endPoint,
        MazeTypeProviderType mazeTypeProviderType
    ) {
        for (SolverType solverType : SolverType.values()) {
            if (solverType != SolverType.ALL) {
                Path solution = solveMaze(maze, startPoint, endPoint, solverType, mazeTypeProviderType);
                gameIORender.print(solverType + DELIMITER + solution.totalCost());
                LOGGER.info("Result for " + solverType + " : total cost = " + solution.totalCost());
            }
        }
    }
}
