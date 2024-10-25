package backend.academy.entity.maze;

import backend.academy.entity.cell.Cell;
import backend.academy.entity.cell.CellType;
import backend.academy.entity.cell.Coordinate;
import backend.academy.entity.edge.Edge;
import backend.academy.entity.edge.EdgeType;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MazeTest {

    @Test
    void testAddAndRetrieveCell() {
        // Arrange
        Maze maze = new Maze();
        Cell cell = new Cell(0, 0, CellType.PASSAGE);

        // Act
        maze.addCell(cell);

        // Assert
        assertTrue(maze.containsCell(cell), "Лабиринт должен содержать добавленную ячейку.");
        assertEquals(CellType.PASSAGE, cell.cellType(), "Тип ячейки должен быть PASSAGE.");
        assertEquals(1, cell.cellType().movementCost(), "Стоимость перемещения для PASSAGE должна быть 1.");
    }

    @Test
    void testAddAndRetrieveEdge() {
        // Arrange
        Maze maze = new Maze();
        Cell cell1 = new Cell(0, 0, CellType.PASSAGE);
        Cell cell2 = new Cell(0, 1, CellType.BAD_SURFACE);
        maze.addCell(cell1);
        maze.addCell(cell2);

        // Act
        maze.addOrUpdateEdge(cell1, cell2, EdgeType.GOOD_TRANSITION);

        // Assert
        Edge edge = maze.getEdge(cell1, cell2);
        assertNotNull(edge, "Ребро должно существовать между ячейками.");
        assertEquals(EdgeType.GOOD_TRANSITION, edge.type(), "Тип ребра должен быть GOOD_TRANSITION.");
        assertTrue(edge.type().isPassable(), "GOOD_TRANSITION должно быть проходимым.");
    }

    @Test
    void testUpdateEdgeType() {
        // Arrange
        Maze maze = new Maze();
        Cell cell1 = new Cell(0, 0, CellType.PASSAGE);
        Cell cell2 = new Cell(0, 1, CellType.GOOD_SURFACE);
        maze.addCell(cell1);
        maze.addCell(cell2);
        maze.addOrUpdateEdge(cell1, cell2, EdgeType.BAD_TRANSITION);

        // Act
        maze.updateEdgeType(cell1, cell2, EdgeType.TRANSITION);

        // Assert
        Edge edge = maze.getEdge(cell1, cell2);
        assertNotNull(edge, "Ребро должно существовать.");
        assertEquals(EdgeType.TRANSITION, edge.type(), "Тип ребра должен быть обновлён до TRANSITION.");

        // Проверка обратного ребра
        Edge reverseEdge = maze.getEdge(cell2, cell1);
        assertNotNull(reverseEdge, "Обратное ребро должно существовать.");
        assertEquals(EdgeType.TRANSITION, reverseEdge.type(),
            "Тип обратного ребра должен быть также обновлён до TRANSITION.");
    }

    @Test
    void testGetNeighbors() {
        // Arrange
        Maze maze = new Maze();
        Cell cell1 = new Cell(0, 0, CellType.PASSAGE);
        Cell cell2 = new Cell(0, 1, CellType.BAD_SURFACE);
        Cell cell3 = new Cell(1, 0, CellType.GOOD_SURFACE);
        maze.addCell(cell1);
        maze.addCell(cell2);
        maze.addCell(cell3);
        maze.addOrUpdateEdge(cell1, cell2, EdgeType.GOOD_TRANSITION);
        maze.addOrUpdateEdge(cell1, cell3, EdgeType.WALL);

        // Act
        List<Cell> neighbors = maze.getNeighbors(cell1);

        // Assert
        assertEquals(1, neighbors.size(), "Должен быть один проходимый сосед.");
        assertTrue(neighbors.contains(cell2), "cell2 должен быть проходимым соседом.");
        assertEquals(CellType.BAD_SURFACE, cell2.cellType(), "Тип cell2 должен быть BAD_SURFACE.");
    }

    @Test
    void testRemoveEdge() {
        // Arrange
        Maze maze = new Maze();
        Cell cell1 = new Cell(0, 0, CellType.PASSAGE);
        Cell cell2 = new Cell(0, 1, CellType.GOOD_SURFACE);
        maze.addCell(cell1);
        maze.addCell(cell2);
        maze.addOrUpdateEdge(cell1, cell2, EdgeType.BAD_TRANSITION);

        // Act
        maze.removeEdge(cell1, cell2);

        // Assert
        assertNull(maze.getEdge(cell1, cell2), "Ребро должно быть удалено.");
        assertNull(maze.getEdge(cell2, cell1), "Обратное ребро также должно быть удалено.");
    }

    @Test
    void testGetAllCells() {
        // Arrange
        Maze maze = new Maze();
        Cell cell1 = new Cell(0, 0, CellType.PASSAGE);
        Cell cell2 = new Cell(0, 1, CellType.BAD_SURFACE);
        maze.addCell(cell1);
        maze.addCell(cell2);

        // Act
        Set<Cell> allCells = maze.getAllCells();

        // Assert
        assertEquals(2, allCells.size(), "Все добавленные ячейки должны быть в лабиринте.");
        assertTrue(allCells.contains(cell1), "Должен содержать cell1.");
        assertTrue(allCells.contains(cell2), "Должен содержать cell2.");
        assertEquals(CellType.PASSAGE, cell1.cellType(), "cell1 должен иметь тип PASSAGE.");
        assertEquals(CellType.BAD_SURFACE, cell2.cellType(), "cell2 должен иметь тип BAD_SURFACE.");
    }

    @Test
    void testDuplicateEdgeAddition() {
        // Arrange
        Maze maze = new Maze();
        Cell cell1 = new Cell(0, 0, CellType.PASSAGE);
        Cell cell2 = new Cell(0, 1, CellType.PASSAGE);
        maze.addCell(cell1);
        maze.addCell(cell2);
        maze.addOrUpdateEdge(cell1, cell2, EdgeType.GOOD_TRANSITION);

        // Act
        maze.addOrUpdateEdge(cell1, cell2, EdgeType.BAD_TRANSITION);

        // Assert
        Edge edge = maze.getEdge(cell1, cell2);
        assertNotNull(edge, "Ребро должно существовать между ячейками.");
        assertEquals(EdgeType.BAD_TRANSITION, edge.type(), "Тип рёбра должен быть обновлен до BAD_TRANSITION.");
    }

    @Test
    void testEdgeRemovalAndReverseEdge() {
        // Arrange
        Maze maze = new Maze();
        Cell cell1 = new Cell(0, 0, CellType.PASSAGE);
        Cell cell2 = new Cell(0, 1, CellType.PASSAGE);
        maze.addCell(cell1);
        maze.addCell(cell2);
        maze.addOrUpdateEdge(cell1, cell2, EdgeType.GOOD_TRANSITION);

        // Act
        maze.removeEdge(cell1, cell2);

        // Assert
        assertNull(maze.getEdge(cell1, cell2), "Ребро должно быть удалено.");
        assertNull(maze.getEdge(cell2, cell1), "Обратное ребро также должно быть удалено.");
    }

    @Test
    void testGetNeighborsWithPassableEdgesOnly() {
        // Arrange
        Maze maze = new Maze();
        Cell cell1 = new Cell(0, 0, CellType.PASSAGE);
        Cell cell2 = new Cell(0, 1, CellType.BAD_SURFACE);
        Cell cell3 = new Cell(1, 0, CellType.GOOD_SURFACE);
        maze.addCell(cell1);
        maze.addCell(cell2);
        maze.addCell(cell3);
        maze.addOrUpdateEdge(cell1, cell2, EdgeType.GOOD_TRANSITION);
        maze.addOrUpdateEdge(cell1, cell3, EdgeType.WALL);

        // Act
        List<Cell> neighbors = maze.getNeighbors(cell1);

        // Assert
        assertEquals(1, neighbors.size(), "Должен быть один проходимый сосед.");
        assertTrue(neighbors.contains(cell2), "cell2 должен быть проходимым соседом.");
    }

    @Test
    void testGetCellWithNonExistentCoordinates() {
        // Arrange
        Maze maze = new Maze();
        Cell cell1 = new Cell(0, 0, CellType.PASSAGE);
        maze.addCell(cell1);

        // Act
        Cell result = maze.getCell(new Coordinate(10, 10));

        // Assert
        assertNull(result, "Должен вернуть null для несуществующих координат.");
    }

    @Test
    void testGetAllCellsAfterEdgeOperations() {
        // Arrange
        Maze maze = new Maze();
        Cell cell1 = new Cell(0, 0, CellType.PASSAGE);
        Cell cell2 = new Cell(0, 1, CellType.PASSAGE);
        maze.addCell(cell1);
        maze.addCell(cell2);

        // Act
        maze.addOrUpdateEdge(cell1, cell2, EdgeType.GOOD_TRANSITION);
        maze.removeEdge(cell1, cell2);
        Set<Cell> allCells = maze.getAllCells();

        // Assert
        assertEquals(2, allCells.size(), "Количество ячеек должно оставаться равным 2 после операций над рёбрами.");
        assertTrue(allCells.contains(cell1), "Лабиринт должен содержать cell1.");
        assertTrue(allCells.contains(cell2), "Лабиринт должен содержать cell2.");
    }

    @Test
    void testNeighborsAfterEdgeTypeUpdate() {
        // Arrange
        Maze maze = new Maze();
        Cell cell1 = new Cell(0, 0, CellType.PASSAGE);
        Cell cell2 = new Cell(0, 1, CellType.PASSAGE);
        maze.addCell(cell1);
        maze.addCell(cell2);
        maze.addOrUpdateEdge(cell1, cell2, EdgeType.GOOD_TRANSITION);

        // Act
        maze.updateEdgeType(cell1, cell2, EdgeType.WALL);
        List<Cell> neighbors = maze.getNeighbors(cell1);

        // Assert
        assertTrue(neighbors.isEmpty(), "Не должно быть проходимых соседей после установки рёбра в WALL.");
    }
}
