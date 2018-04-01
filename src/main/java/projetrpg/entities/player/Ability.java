package projetrpg.entities.player;

import projetrpg.Describable;

/**
 * Created by mhevin on 29/03/18.
 */
public class Ability implements Describable {
    private String name;
    private int minLevelRequired;
    private double damage;
    private AttackType type;

    public Ability(String name, int minLevelRequired, double damage, AttackType type) {
        this.name = name;
        this.minLevelRequired = minLevelRequired;
        this.damage = damage;
        this.type = type;
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
}
