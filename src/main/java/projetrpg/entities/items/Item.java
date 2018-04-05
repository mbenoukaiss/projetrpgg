package projetrpg.entities.items;

import projetrpg.Describable;
import projetrpg.entities.Entity;
import projetrpg.map.Region;

/**
 * An item.
 */
public class Item implements Describable {

    /**
     * The amount of additional damage this
     * item should deal if used as weapon.
     */
    private int damage;

    /**
     * The name of the item.
     */
    private String name;

    /**
     * The type of the item.
     */
    private ItemType type;

    public Item(String name, int damage, ItemType type) {
        this.name = name;
        this.damage = damage;
        this.type = type;
    }

    @Override
    public String describe() {
        return this.name + ", damages : " + this.damage + ", type : " + this.type;
    }

    /**
     * Accessor for the amount of additional
     * damage.
     *
     * @return The damage.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Accessor for the name of the item.
     * @return the name.
     */
    public String getName() {
        return this.name;
    }

    public ItemType getType() {
        return type;
    }
}
