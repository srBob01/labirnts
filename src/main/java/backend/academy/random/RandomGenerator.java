package backend.academy.random;

public interface RandomGenerator {
    int nextInt(int bound);
    int nextInt(int min, int max);
    boolean nextBoolean();
}
