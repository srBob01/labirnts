package backend.academy.random;

public class FixedRandomGenerator implements RandomGenerator {
    @Override
    public int nextInt(int bound) {
        return 0;
    }

    @Override
    public int nextInt(int min, int max) {
        return min;
    }

    @Override
    public boolean nextBoolean() {
        return false;
    }
}
