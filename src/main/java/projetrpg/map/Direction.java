package projetrpg.map;

/**
 * The four directions the player
 * can move in.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public enum Direction {
    SOUTH,
    NORTH,
    EST,
    WEST;

    /**
     * Getter for the opposite direction
     * of this direction.
     *
     * @return The opposite direction
     */
    public Direction opposite() {
        switch(this) {
            case NORTH:
                return SOUTH;
            case EST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EST;
        }

        return null;
    }

}
