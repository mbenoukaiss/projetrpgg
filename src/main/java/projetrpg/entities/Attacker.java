package projetrpg.entities;

/**
 * Anything that can deal damage.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public interface Attacker {
    public boolean attack(Damageable target, int damages);
    public int baseDamage();
}
