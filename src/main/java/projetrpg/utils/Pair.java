package projetrpg.utils;

import java.util.Objects;

public class Pair<T, U> {

    public final T first;
    public final U second;

    /**
     * Constructor for a pair.
     *
     * @param first The first object
     * @param second The second object
     */
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Method for creating a pair.
     *
     * @param first The first object
     * @param second The second object
     * @param <T> The type of the first object
     * @param <U> The type of the second object
     * @return The pair.
     */
    public static <T, U> Pair<T, U> of(T first, U second) {
        return new Pair<>(first, second);
    }

    /**
     * Check if two pairs are equal by calling the equals
     * method of each object.
     *
     * @param o The other pair
     * @return True if they're equal.
     */
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;

        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

}
