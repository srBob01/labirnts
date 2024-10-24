package backend.academy.mazetype;

import backend.academy.random.RandomGenerator;

/**
 * Фабрика для провайдера типа ячеек лабиринта.
 */
public class MazeTypeProviderFactory {

    private final MazeTypeProvider simpleProvider;
    private final MazeTypeProvider advancedProvider;

    public MazeTypeProviderFactory(RandomGenerator randomGenerator) {
        this.simpleProvider = new SimpleMazeTypeProvider();
        this.advancedProvider = new AdvancedMazeTypeProvider(randomGenerator);
    }

    /**
     * Возвращает провайдер на основе типа.
     *
     * @param type Тип провайдера.
     * @return Провайдер типов для ячеек лабиринта.
     */
    public MazeTypeProvider getProvider(MazeTypeProviderType type) {
        return switch (type) {
            case SIMPLE -> simpleProvider;
            case ADVANCED -> advancedProvider;
        };
    }
}
