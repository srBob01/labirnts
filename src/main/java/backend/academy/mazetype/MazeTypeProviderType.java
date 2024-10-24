package backend.academy.mazetype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * Enum для выбора типа провайдера ячеек.
 */
@RequiredArgsConstructor
@Getter
public enum MazeTypeProviderType {
    SIMPLE("Simple cell type, without advanced characteristics"),
    ADVANCED("Advanced cell type with extended characteristics");

    private final String description;

    @Override
    public String toString() {
        return description;
    }
}
