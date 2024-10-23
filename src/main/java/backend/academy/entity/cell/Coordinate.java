package backend.academy.entity.cell;

import java.util.Objects;

public record Coordinate(int row, int col) implements Comparable<Coordinate> {
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coordinate that)) {
            return false;
        }
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public int compareTo(Coordinate o) {
        int rowComparison = Integer.compare(this.row, o.row);
        if (rowComparison != 0) {
            return rowComparison;
        }
        return Integer.compare(this.col, o.col);
    }
}
