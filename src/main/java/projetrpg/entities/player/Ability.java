package projetrpg.entities.player;

import projetrpg.Describable;

/**
 * Represents a special ability of a player.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public class Ability implements Describable {

    /**
     * The name that should be displayed.
     */
    private String name;

    /**
     * The minimum level required before
     * the player can get this ability.
     */
    private int minLevelRequired;

    /**
     * The amount of damages this ability
     * should deal.
     */
    private double damage;

    /**
     * The type of the attack.
     */
    private AttackType type;

    private boolean isLocked;

    private int cost;

    public Ability(String name, int minLevelRequired, double damage, AttackType type, int cost) {
        this.name = name;
        this.minLevelRequired = minLevelRequired;
        this.damage = damage;
        this.type = type;
        this.isLocked = true;
        this.cost = cost;
    }

    @Override
    public String describe() {
        return this.name + " : does " + this.damage + " damages. It is a " + this.type + " attack, and it " +
                "requires lvl " + this.minLevelRequired + " in order to learn it.";
    }

    public String getName() {
        return this.name;
    }

    public int getMinLevelRequired() {
        return this.minLevelRequired;
    }

    public double getDamage() {
        return this.damage;
    }

    public AttackType getType() {
        return this.type;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public int getCost() {
        return cost;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}

