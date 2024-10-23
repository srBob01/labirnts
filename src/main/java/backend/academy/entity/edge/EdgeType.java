package backend.academy.entity.edge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EdgeType {
    WALL(-1),
    BAD_TRANSITION(3),
    GOOD_TRANSITION(1),
    TRANSITION(2);
    private final int movementCost;

    public boolean isPassable() {
        return this != WALL;
    }
}
