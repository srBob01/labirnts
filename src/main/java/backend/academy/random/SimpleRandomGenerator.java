package backend.academy.random;

import java.util.Random;

public class SimpleRandomGenerator implements RandomGenerator {
    private final Random random = new Random();

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    @Override
    public int nextInt(int min, int max) {
        return random.nextInt(min, max);
    }

    @Override
    public boolean nextBoolean() {
        return random.nextBoolean();
    }
}
