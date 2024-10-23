package backend.academy.entity.cell;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Cell {
    private final Coordinate coordinate;
    @Setter
    private CellType cellType;

    public Cell(int row, int col, CellType cellType) {
        coordinate = new Coordinate(row, col);
        this.cellType = cellType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cell cell)) {
            return false;
        }
        return Objects.equals(coordinate, cell.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate, cellType);
    }
}
