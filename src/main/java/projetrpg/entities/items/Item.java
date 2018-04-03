package projetrpg.entities.items;

import projetrpg.Describable;
import projetrpg.entities.Entity;
import projetrpg.map.Region;

/**
 * An item.
 */
public class Item extends Entity implements Describable {

    /**
     * The amount of additional damage this
     * item should deal if used as weapon.
     */
    private int damage;

    /**
     * The type of the item.
     */
    private ItemType type;

    /**
     * Where the items originates from.
     */
    private Region defaultLocation;

    public Item(String name, int damage, ItemType type, Region defaultLocation) {
        super(name, defaultLocation);
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

}
