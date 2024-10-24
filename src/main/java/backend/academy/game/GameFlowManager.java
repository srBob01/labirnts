package backend.academy.game;

public class GameFlowManager {

    /**
     * Переход к следующему состоянию в зависимости от текущего состояния.
     */
    public GameStateType next(GameStateType currentState) {
        return
            switch (currentState) {
                case INPUT_PARAMETERS -> GameStateType.GENERATE_MAZE;
                case GENERATE_MAZE -> GameStateType.SELECT_POINTS;
                case SELECT_POINTS -> GameStateType.SELECT_SOLUTION;
                case SELECT_SOLUTION -> GameStateType.OUTPUT_RESULTS;
                case OUTPUT_RESULTS -> GameStateType.CHOOSE_NEXT_ACTION;
                case CHOOSE_NEXT_ACTION, FINISH -> null;
            };
    }

    /**
     * Начинает действие игры.
     */
    public GameStateType startGame() {
        return GameStateType.INPUT_PARAMETERS;
    }

    /**
     * Проверка завершения игры.
     */
    public boolean isFinished(GameStateType currentState) {
        return currentState == GameStateType.FINISH;
    }

    /**
     * Метод для ручного завершения игры.
     */
    public GameStateType finishGame() {
        return GameStateType.FINISH;
    }
}
