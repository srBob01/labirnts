package backend.academy.entity.cell;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CellType {
    PASSAGE(1),
    BAD_SURFACE(2),
    GOOD_SURFACE(0);

    private final int movementCost;
}
