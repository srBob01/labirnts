package backend.academy.utils;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.cell.Coordinate;
import backend.academy.entity.path.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ReconstructorPath {
    /**
     * Вспомогательный метод для восстановления пути.
     *
     * @param predecessors Карта предшественников.
     * @param endCell      Конечная ячейка.
     * @param totalCost    Общая стоимость пути.
     * @return Путь и его стоимость.
     */
    public Path reconstruct(Map<Cell, Cell> predecessors, Cell endCell, int totalCost) {
        List<Coordinate> path = new ArrayList<>();
        Cell current = endCell;

        while (current != null) {
            path.add(current.coordinate());
            current = predecessors.get(current);
        }

        Collections.reverse(path);
        return new Path(path, totalCost);
    }
}
