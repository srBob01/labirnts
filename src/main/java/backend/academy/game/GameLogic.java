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
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameLogic {

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
            currentState = switch (currentState) {
                case INPUT_PARAMETERS -> {
                    height =
                        gameIORender.readIntWithRetries(NUMBER_ATTEMPT, MIN_SIDE_MAZE, MAX_SIDE_MAZE,
                            "Enter height");
                    width =
                        gameIORender.readIntWithRetries(NUMBER_ATTEMPT, MIN_SIDE_MAZE, MAX_SIDE_MAZE,
                            "Enter width");

                    mazeTypeProviderType = gameIORender.selectMazeTypeProvider();

                    generatorType = gameIORender.selectMazeGeneratorType();

                    addCycles = generatorType != MazeGeneratorType.RECURSIVE_DIVISION
                                && gameIORender.readBoolean("Should cycles be added? (0 - no, anything else - yes): ");

                    if (addCycles) {
                        cycleLevelType = gameIORender.selectCycleLevelType();
                    }

                    yield gameFlowManager.next(currentState);
                }
                case GENERATE_MAZE -> {
                    assert mazeTypeProviderType != null;
                    var mazeTypeProvider = mazeTypeProviderFactory.getProvider(mazeTypeProviderType);
                    assert generatorType != null;
                    var generator = mazeGeneratorFactory.getGenerator(generatorType);

                    maze = generator.generate(height, width, mazeTypeProvider);

                    if (addCycles) {
                        cycleAdder.addCycles(maze, mazeTypeProvider, cycleLevelType);
                    }

                    gameIORender.print(render.render(maze));

                    yield gameFlowManager.next(currentState);
                }
                case SELECT_POINTS -> {
                    boolean randomSelection = gameIORender.readBoolean(
                        "Select a random starting point? (0 - no, anything else - yes): ");

                    if (randomSelection) {
                        startPoint = boundarySelector.selectRandomFromFirstRow(maze, width);
                        endPoint = boundarySelector.selectRandomFromLastRow(maze, height, width);
                    } else {
                        startPoint = gameIORender.readCoordinates(NUMBER_ATTEMPT, MIN_SIDE_MAZE, width, height);
                        endPoint = gameIORender.readCoordinates(NUMBER_ATTEMPT, MIN_SIDE_MAZE, width, height);
                    }

                    gameIORender.print("Start point:" + startPoint);
                    gameIORender.print("End point" + endPoint);

                    yield gameFlowManager.next(currentState);
                }
                case SELECT_SOLUTION -> {
                    solverType = gameIORender.selectSolverType();
                    yield gameFlowManager.next(GameStateType.SELECT_SOLUTION);
                }
                case OUTPUT_RESULTS -> {
                    if (solverType == SolverType.ALL) {
                        outputResultsForAllSolutions(maze, startPoint, endPoint, mazeTypeProviderType);
                    } else {
                        outputResults(maze, startPoint, endPoint, solverType, mazeTypeProviderType);
                    }
                    yield gameFlowManager.next(currentState);
                }
                case CHOOSE_NEXT_ACTION -> gameIORender.selectGameState();
                case FINISH -> gameFlowManager.finishGame();
            };
        }
    }

    /**
     * Решение лабиринта выбранным способом.
     */
    private Path solveMaze(
        Maze maze, Coordinate startPoint, Coordinate endPoint, SolverType solverType,
        MazeTypeProviderType mazeTypeProviderType
    ) {
        var solver = solverFactory.getSolver(solverType);
        return solver.solve(maze, startPoint, endPoint, mazeTypeProviderFactory.getProvider(mazeTypeProviderType));
    }

    /**
     * Отображение решения и его стоимости.
     */
    private void outputResults(
        Maze maze, Coordinate startPoint, Coordinate endPoint, SolverType solverType,
        MazeTypeProviderType mazeTypeProviderType
    ) {
        Path result = solveMaze(maze, startPoint, endPoint, solverType, mazeTypeProviderType);
        gameIORender.print(render.render(maze, result.coordinates()));
        gameIORender.print(solverType.toString() + DELIMITER + result.totalCost());

    }

    /**
     * Вывод решений для всех возможных алгоритмов.
     */
    private void outputResultsForAllSolutions(
        Maze maze, Coordinate startPoint, Coordinate endPoint,
        MazeTypeProviderType mazeTypeProviderType
    ) {
        for (SolverType solverType : SolverType.values()) {
            if (solverType != SolverType.ALL) {
                Path solution = solveMaze(maze, startPoint, endPoint, solverType, mazeTypeProviderType);
                gameIORender.print(solverType.toString() + DELIMITER + solution.totalCost());
            }
        }
    }
}
