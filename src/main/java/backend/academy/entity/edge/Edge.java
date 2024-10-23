package backend.academy.entity.edge;

import backend.academy.entity.cell.Cell;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Edge {
    private Cell from;
    private Cell to;
    @Setter
    private EdgeType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edge edge)) {
            return false;
        }
        return Objects.equals(from, edge.from) && Objects.equals(to, edge.to)
               && type == edge.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, type);
    }
}
