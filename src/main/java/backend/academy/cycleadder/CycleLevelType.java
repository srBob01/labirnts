package backend.academy.cycleadder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum для уровней циклизации лабиринта.
 * Содержит разные уровни с соответствующими процентами для добавления циклов.
 */
@RequiredArgsConstructor
@Getter
public enum CycleLevelType {
    LOW(10),
    MEDIUM(20),
    HIGH(30);

    private final int cycleProbability;
}
