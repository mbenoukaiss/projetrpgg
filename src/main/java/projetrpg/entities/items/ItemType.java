package projetrpg.entities.items;

/**
 * Represents the different types of an item.
 *
 * @author mhevin
 * @author mbenoukaiss
 */
public enum ItemType {
    DMG(0),
    UTILS(0),
    FOOD(10),
    MARTIAN_FOOD(20),
    HEAL(15);

    /**
     * The hp an item gives when eaten.
     */
    private final int hpGiven;

    ItemType(int hpGiven) {
        this.hpGiven = hpGiven;
    }

    /**
     * Accessor for the hp an item gives when eaten.
     * @return the hp.
     */
    public int getHpGiven() {
        return hpGiven;
    }
}
