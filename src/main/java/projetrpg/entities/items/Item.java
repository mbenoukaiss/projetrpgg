package projetrpg.entities.items;

import projetrpg.Describable;
import projetrpg.entities.Entity;
import projetrpg.map.Region;

/**
 * Enumeration of all the items available in
 * the game.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public enum Item implements Describable {
    APPLE("Apple", 0, ItemType.FOOD),
    GREEN_CARROT("Green carrot", 0, ItemType.MARTIAN_FOOD),
    GREEN_POTATO("Green potato", 0, ItemType.MARTIAN_FOOD),
    FLASHLIGHT("Flashlight", 10, ItemType.UTILS),
    KNIFE("Knife", 35, ItemType.UTILS),
    TOOLKIT("Toolkit", 3, ItemType.UTILS),
    LASERGUN("Laser Gun", 20, ItemType.DMG),
    SCREWDRIVER("Screwdriver", 1, ItemType.UTILS),
    HATCHET("Hatchet", 40, ItemType.DMG),
    SCREW("Screw", 1, ItemType.UTILS),
    METAL_SCRAP("Metal scrap", 4, ItemType.UTILS),
    CLIMBING_SHOES("Climbind shoes", 1, ItemType.UTILS),
    SHARP_SHIP_FRAGMENT("Sharp ship fragment", 50, ItemType.DMG),
    MARTIAN_SWORD("Martian Swordן", 65, ItemType.DMG),
    LEGENDARY_BOOK("Legendary book", 0, ItemType.UTILS);

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

    Item(String name, int damage, ItemType type) {
        this.name = name;
        this.damage = damage;
        this.type = type;
    }

    /**
     * Ask this object to describe itself.
     * @return the description as a string.
     */
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

    /**
     * Accessor for the type of the item.
     * @return the type.
     */
    public ItemType getType() {
        return type;
    }
}
