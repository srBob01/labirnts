# Maze Game - Генерация и Решение Лабиринтов

**Maze Game** — это игра, в которой вы можете генерировать лабиринты с использованием различных алгоритмов, добавлять
туда циклы, выбирать начальные и конечные точки, а затем решать их с помощью различных методов поиска пути.

## Структура проекта

Проект состоит из следующих ключевых элементов:

- **[GameLogic](./src/main/java/backend/academy/game/GameLogic.java)** — Основной класс игры, который управляет этапами
  игры.
- **Генераторы лабиринтов** — классы, которые реализуют различные алгоритмы генерации:
    - **[Алгоритм Прима](./src/main/java/backend/academy/generator/prime/PrimMazeGenerator.java)**
    - **[Алгоритм Крускала](./src/main/java/backend/academy/generator/kruskal/KruskalMazeGenerator.java)**
    - **[Алгоритм растущего дерева](./src/main/java/backend/academy/generator/growingtree/GrowingTreeMazeGenerator.java)**
    - **[Алгоритм Охоты и Убийства](./src/main/java/backend/academy/generator/huntandkill/HuntAndKillMazeGenerator.java)**
    - **[Рекурсивное деление](./src/main/java/backend/academy/generator/recursivedivision/RecursiveDivisionMazeGenerator.java)**
- **Алгоритмы решения** — реализуют различные методы поиска пути:
    - **[AStar](./src/main/java/backend/academy/solver/priority/AStarSolver.java)**
    - **[Dijkstra](./src/main/java/backend/academy/solver/priority/DijkstraSolver.java)**
    - **[BFS](./src/main/java/backend/academy/solver/fs/BFSSolver.java)**
    - **[DFS](./src/main/java/backend/academy/solver/fs/DFSSolver.java)**
    - **[BiDirection](./src/main/java/backend/academy/solver/bidirection/BiDirectionalSolver.java)**

- **[pom.xml](./pom.xml)** — Дескриптор сборки Maven, описывающий зависимости.

## Как играть

1. **Ввод размеров лабиринта**  
   Введите высоту и ширину лабиринта. Например:
   ```plaintext
   Enter height
   > 3
   Enter width
   > 15
   ```

2. **Выбор типа ячеек**  
   Выберите тип ячеек, который будет использоваться в лабиринте:
   ```plaintext
   Select Maze Type Provider.
   0 - Simple cell type, without advanced characteristics
   1 - Advanced cell type with extended characteristics
   other - random
   > 1
   Your choice Maze Type Provider: Advanced cell type with extended characteristics
   ```

3. **Выбор алгоритма генерации лабиринта**  
   Выберите алгоритм, который будет использоваться для генерации лабиринта:
   ```plaintext
   Select Maze Generator.
   0 - Growing Tree Algorithm
   1 - Hunt and Kill Algorithm
   2 - Kruskal's Algorithm
   3 - Prim's Algorithm
   4 - Recursive Division Algorithm
   other - random
   > 3
   Your choice Maze Generator: Prim's Algorithm
   ```

4. **Добавление циклов (опционально)**  
   Можно добавить циклы в лабиринт для усложнения структуры:
   ```plaintext
   Should cycles be added? (0 - no, anything else - yes)
   > 1
   ```

5. **Добавление циклов (опционально)**  
   Насколько много добавлять циклов:
   ```plaintext
   Select Cycle level
    0 - LOW
    1 - MEDIUM
    2 - HIGH
    other - random.
    > 2
    +---+---+---+
    |   =     B |
    + ~ + = + ~ +
    |   ~ B ~ G |
    + ~ + = +   +
    | B |   ~ G |
    +---+---+---+
     ```

6. **Выбор начальных и конечных точек**  
   Укажите, хотите ли вы выбрать начальные и конечные точки вручную или доверить это программе:
   ```plaintext
   Select a random starting point? (0 - no, anything else - yes)
   > 1
   Start point: Coordinate[row=0, col=1]
   End point: Coordinate[row=2, col=0]
   ```

7. **Выбор алгоритма решения лабиринта**  
   Выберите один из алгоритмов решения:
   ```plaintext
   0 - Breadth-First Search (BFS)
   1 - Depth-First Search (DFS)
   2 - Dijkstra's Algorithm
   3 - A* Algorithm
   4 - Bidirectional Search
   5 - All Algorithms
   other - random
   > 3
   Your choice Solver Type: A* Algorithm
   ```

8. **Вывод результатов**  
   Игра выведет сгенерированный лабиринт и решение с общей стоимостью пути:
   ```plaintext
    +---+---+---+
    | * * *   B |
    + * + = + ~ +
    | * ~ B ~ G |
    + * + = +   +
    | * |   ~ G |
    +---+---+---+
   A* Algorithm : 9
   ```
9. **Дальнейшие действия**
   Далее необходимо решить что дальше, и игра начнется с этого места:
```plaintext
    0 - Input maze parameters
    1 - Generate maze
    2 - Select start and end points
    3 - Select maze solving method
    4 - Show solution results
    5 - Choose next action
    6 - Finish the game
    other - random.
   ```
