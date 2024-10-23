package backend.academy.entity.cell;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CellType {
    PASSAGE(1),          // Обычный проход
    BAD_SURFACE(2),      // Плохое покрытие (например, болото)
    GOOD_SURFACE(0);     // Хорошее покрытие (например, монетки)

    private final int movementCost;
}
