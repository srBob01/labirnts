package backend.academy.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GameStateType {
    INPUT_PARAMETERS("Input maze parameters"),
    GENERATE_MAZE("Generate maze"),
    SELECT_POINTS("Select start and end points"),
    SELECT_SOLUTION("Select maze solving method"),
    OUTPUT_RESULTS("Show solution results"),
    CHOOSE_NEXT_ACTION("Choose next action"),
    FINISH("Finish the game");

    private final String description;

    @Override
    public String toString() {
        return description;
    }
}
