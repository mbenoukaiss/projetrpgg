package projetrpg.entities;

/**
 * Anything that can tae damage.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public interface Damageable {
    public boolean damage(int value);
    public int getHp();
}
