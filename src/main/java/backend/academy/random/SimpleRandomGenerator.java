package backend.academy.random;

import java.security.SecureRandom;

public class SimpleRandomGenerator implements RandomGenerator {
    private final SecureRandom random;

    public SimpleRandomGenerator() {
        random = new SecureRandom();
    }

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
