package projetrpg.entities;

/**
 * Anything that can tae damage.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public interface Damageable {

    /**
     * Method called when this entity
     * should take damages.
     *
     * @param value The amount of damages
     *              it should take.
     * @return True if it died.
     */
    boolean damage(int value);

    /**
     * Return the amount of HP.
     *
     * @return The amount of HP.
     */
    int getHp();
}
