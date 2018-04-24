package projetrpg.entities.items;

/**
 * Created by mhevin on 29/03/18.
 *
 * Represents the different types of an item.
 */
public enum ItemType {
    DMG(0),
    UTILS(0),
    FOOD(10);

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
