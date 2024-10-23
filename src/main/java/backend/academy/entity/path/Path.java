package backend.academy.entity.path;

import backend.academy.entity.cell.Coordinate;
import java.util.List;
import java.util.StringJoiner;

public record Path(List<Coordinate> coordinates, int totalCost) {
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(" -> ", "Path: [", "]\n");

        for (Coordinate coordinate : coordinates) {
            joiner.add(coordinate.toString());
        }

        return String.format("%sTotal Cost: %d", joiner, totalCost);
    }
}
