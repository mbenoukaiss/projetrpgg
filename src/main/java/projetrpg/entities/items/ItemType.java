package projetrpg.entities.items;

/**
 * Created by mhevin on 29/03/18.
 */
public enum ItemType {
    DMG(0),
    UTILS(0),
    FOOD(10);

    private final int hpGiven;

    ItemType(int hpGiven) {
        this.hpGiven = hpGiven;
    }

    public int getHpGiven() {
        return hpGiven;
    }
}
