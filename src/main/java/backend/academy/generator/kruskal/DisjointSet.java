package backend.academy.generator.kruskal;

import java.util.HashMap;
import java.util.Map;

public class DisjointSet<T> {
    private final Map<T, T> parent = new HashMap<>();

    /**
     * Создаёт новое множество для элемента.
     */
    public void makeSet(T item) {
        parent.put(item, item);
    }

    /**
     * Находит представителя множества, используя сжатие пути.
     */
    public T findSet(T item) {
        T p = parent.get(item);
        if (!p.equals(item)) {
            T root = findSet(p);
            parent.put(item, root); // Сжатие пути
            return root;
        }
        return p;
    }

    /**
     * Объединяет два множества.
     */
    public void union(T item1, T item2) {
        T root1 = findSet(item1);
        T root2 = findSet(item2);
        if (!root1.equals(root2)) {
            parent.put(root1, root2);
        }
    }
}

