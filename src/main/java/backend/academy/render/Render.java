package backend.academy.render;

import backend.academy.entity.cell.Coordinate;
import backend.academy.entity.maze.Maze;
import java.util.List;

public interface Render {
    String render(Maze maze);

    String render(Maze maze, List<Coordinate> path);

}
