package projetrpg.entities;

/**
 * Anything that can deal damage.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public interface Attacker {

    /**
     * Attacks a damageable.
     *
     * @param target The thing to attack.
     * @param damages The amount of damage to deal.
     * @return True if the other died.
     */
    boolean attack(Damageable target, int damages);

    /**
     * Getter for the base damage of this attacker.
     *
     * @return The base damage
     */
    int baseDamage();
}
