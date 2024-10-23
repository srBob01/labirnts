package backend.academy.entity.wall;

import backend.academy.entity.cell.Cell;

/**
 * Вспомогательный класс Wall используется для представления стены между двумя ячейками (Cell) в алгоритмах генерации
 * и поиска лабиринтов. Он хранит текущую ячейку и соседнюю ячейку, которые разделены стеной.
 */
public record Wall(Cell current, Cell neighbor) {
}
